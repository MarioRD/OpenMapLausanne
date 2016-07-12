
package ch.epfl.imhof.osm;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 * A class (with a private constructor) containing a method that creates and return an OSMMap according a XML-syntaxed specified file.
 */

import java.util.zip.GZIPInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;

/**
 * A class (with a private constructor) containing a method that creates and return an OSMMap according a XML-syntaxed specified file.
 */
public final class OSMMapReader {


	private OSMMapReader()
	{  }

	/**
	 * method that creates and return a OSMMap according a XML-syntaxed specified file.
	 * @param fileName, name of the file the method will read.
	 * @param unGZip, specify if the file is a Zip compressed file or not with respectively true and false.
	 * @return an OSMMap build according the XML-syntaxed specified file.
	 * @throws IOException if their is an error with the reading of the specified file.
	 * @throws SAXException if their is an error of XML syntax in the content of the specified file.
	 */
	public static OSMMap readOSMFile(String fileName, boolean unGZip) throws IOException, SAXException
	{
		final OSMMap.Builder map = new OSMMap.Builder();

		final InputStream i;
		if (unGZip)
		{
			i = new GZIPInputStream(new FileInputStream(fileName));
		}
		else  
		{
			i = new  FileInputStream(fileName);
		}

		XMLReader r = XMLReaderFactory.createXMLReader();
		r.setContentHandler(new DefaultHandler() {
			private OSMEntity.Builder currentOSMEntity=null;

			@Override
			public void startElement(String uri,
					String lName,
					String qName,
					Attributes atts)
							throws SAXException
			{
				switch(qName){
				case "way": createWay(atts);
				break;

				case "relation": createRelation(atts);
				break;

				case "node": createNode(atts);
				break;

				case "member" : addOSMMember(atts);
				break;

				case "tag": addOSMKey(atts);
				break;

				case "nd": addOSMNode(atts);
				break;

				default: break;
				}

			}

			/**
			 * @param atts, attributes that specify the characteristics of the OSMWay.Builder we creates in this method.
			 */
			final private void createWay(Attributes atts)
			{
				if(currentOSMEntity==(null))
				{
					try{
						final Long wayId=Long.parseLong(atts.getValue("id"));
						if(!wayId.equals(null))
						{
							final OSMWay.Builder OSMWay = new OSMWay.Builder(wayId);
							currentOSMEntity = OSMWay;
						}
					}
					catch (NumberFormatException e)
					{
					}
				}
			}

			/**
			 * @param atts, attributes that specify the characteristics of the OSMRelation.Builder we creates in this method.
			 */
			final private void createRelation(Attributes atts)
			{
				if(currentOSMEntity==(null))
				{
					try
					{
						final Long relationId=Long.parseLong(atts.getValue("id"));
						if(!relationId.equals(null))
						{
							final OSMRelation.Builder OSMRelation = new OSMRelation.Builder(relationId);
							currentOSMEntity = OSMRelation;
						}
					}
					catch (NumberFormatException e) 
					{
					}
				}
			}

			/**
			 * @param atts, attributes that specify the characteristics of the OSMNode.Builder we creates in this method.
			 */
			final private void createNode(Attributes atts){
				if(currentOSMEntity==(null))
				{
					try{
						final Double nodeLongitude = Double.parseDouble(atts.getValue("lon"));
						final Double nodeLatitude = Double.parseDouble(atts.getValue("lat"));
						final Long nodeId=Long.parseLong(atts.getValue("id"));
						if(!nodeId.equals(null) && !nodeLatitude.equals(null) && !nodeLongitude.equals(null))
						{
							final OSMNode.Builder OSMNode = new OSMNode.Builder(nodeId,
									new PointGeo(Math.toRadians(nodeLongitude),
											Math.toRadians(nodeLatitude))
									);

							currentOSMEntity = OSMNode;
						}
					} 
					catch (NumberFormatException e) 
					{
					}
				}
			}

			/**
			 * @param atts, attributes that specify the identifiant of the node we'll add to the currentOSMEntity in this method.
			 */
			private void addOSMNode(Attributes atts)
			{
				if(currentOSMEntity!=null){
					if(currentOSMEntity instanceof OSMWay.Builder)
					{
						String sTMP;
						sTMP = (atts.getValue("ref"));
						long idTMP = Long.parseLong(sTMP);
						OSMNode nodeTMP = map.nodeForId(idTMP);
						if(nodeTMP!=(null))
						{
							((OSMWay.Builder) currentOSMEntity).addNode(nodeTMP);
						}else{
							currentOSMEntity.setIncomplete();
						}

					}
				}
			}

			/**
			 * @param atts, attributes that specify the identifiant of the member we'll add to the currentOSMEntity in this method.
			 */
			final private void addOSMMember(Attributes atts)
			{
				if(currentOSMEntity!=null){
					if (currentOSMEntity instanceof OSMRelation.Builder)
					{
						if(atts.getValue("type")!=null)
						{
							OSMEntity entity;
							switch(atts.getValue("type"))
							{

							case "node" : 
								entity =  map.nodeForId(Long.parseLong(atts.getValue("ref")));

								if(entity!=null){

									((OSMRelation.Builder)currentOSMEntity).addMember
									(OSMRelation.Member.Type.NODE,
											atts.getValue("role"),
											entity
											);
								}else{
									currentOSMEntity.setIncomplete();
								}
								break;

							case "way" : 
								entity =  map.wayForId(Long.parseLong(atts.getValue("ref")));

								if(entity!=null){

									((OSMRelation.Builder)currentOSMEntity).addMember
									(OSMRelation.Member.Type.WAY,
											atts.getValue("role"),
											entity
											);
								}else{
									currentOSMEntity.setIncomplete();
								}
								break;

							case "relation" :
								entity =  map.relationForId(Long.parseLong(atts.getValue("ref")));

								if(entity!=null){

									((OSMRelation.Builder)currentOSMEntity).addMember
									(OSMRelation.Member.Type.RELATION,
											atts.getValue("role"),
											entity
											);
								}else{
									currentOSMEntity.setIncomplete();
								}
								break;
							default: break;
							}

						}

					}
				}
			}

			/**
			 * @param atts, specify the OSM attributes we'll attach to the currentOSMEntity in this method.
			 */
			final private void addOSMKey(Attributes atts){
				if(currentOSMEntity!=null){
					final String key=atts.getValue("k");
					final String value=atts.getValue("v");

					if(key!=(null)||value!=(null)){
						currentOSMEntity.setAttribute(key, 
								value);
					}else{
						currentOSMEntity.setIncomplete();
					}
				}
			}

			@Override
			public void endElement(String uri,
					String lName,
					String qName)
			{

				if(currentOSMEntity instanceof OSMWay.Builder && qName=="way")
				{
					try
					{
						if(!currentOSMEntity.isIncomplete()){
							map.addWay(((OSMWay.Builder) currentOSMEntity).build());
						}
					}
					catch (IllegalStateException e )
					{
					}
					currentOSMEntity=null;

				}else if(currentOSMEntity instanceof OSMNode.Builder && qName=="node"){
					if(!currentOSMEntity.isIncomplete()){
						map.addNode(((OSMNode.Builder) currentOSMEntity).build());
					}
					currentOSMEntity=null;

				}else if(currentOSMEntity instanceof OSMRelation.Builder && qName=="relation"){
					if(!currentOSMEntity.isIncomplete()){
						map.addRelation(((OSMRelation.Builder) currentOSMEntity).build());
					}
					currentOSMEntity=null;
				}
				
			}
			
		});

		r.parse(new InputSource(i));
		i.close();
		return map.build();
	}

}