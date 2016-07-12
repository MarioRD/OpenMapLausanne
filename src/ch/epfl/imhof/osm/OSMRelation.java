/**
 * 
 */
package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * @author Mario Robert D'Ambrosio (249757)
 * @author Nicodème Stalder (234584)
 * 
 */
public final class OSMRelation extends OSMEntity {

    private final List<Member> members = new ArrayList<>();

    public OSMRelation(long id, List<Member> members, Attributes attributes){
        super(id,attributes);
        this.members.addAll(members);
    }

    public List<Member> members(){
        return members;
    }
    public final static class Member{

        public static enum Type{
            NODE("nodes"), RELATION("relations"), WAY("chemin");
            private Type(String type){}
        }

        private final Type type;
        private final String role;
        private final OSMEntity member;

        public Member(Type type, String role, OSMEntity member){
            this.type=type;
            this.role=role;
            this.member=member;
        }

        public Type type(){
            return type;
        }

        public String role(){
            return role;
        }

        public OSMEntity member(){
            return member;
        }

    }
    public final static class Builder extends OSMEntity.Builder
    {

        private List<Member> members = new ArrayList<>();

        public Builder(long id){
            super(id);
        }

        public void addMember(Member.Type type, String role, OSMEntity newMember){
            members.add(new Member(type,role,newMember));
        }

        public OSMRelation build(){
            if (super.isIncomplete())
            {
                throw new IllegalStateException();
            }
            return new OSMRelation(this.id,this.members,super.attributes.build());
        }
    }

}
