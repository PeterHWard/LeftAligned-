package left_aligned;

import java.util.ArrayList;

public class ElementGroup implements IElementGroup<ScriptElement> {
	public String type = "Element Group";
    private ArrayList<ScriptElement> members;
    
    public ElementGroup(ScriptElement... elements){
        this.members = new ArrayList<ScriptElement>();
        
        if (elements.length > 0) {
        	for (ScriptElement e : elements) {
        		this.addMember(e);
        	}
        }
    }
    
    public void addMember(ScriptElement newMember) {
        members.add(newMember);
    }
    
    public void removeMember(int idx) {
    	members.remove(idx);
    }
    
    public void removeMember(ScriptElement elem) {
    	members.remove(elem);
    }
    
    public ArrayList<ScriptElement> getMembers(){
        return members;
    }
    
    public ArrayList<Text> getTextRuns() {
    	ArrayList<Text> retVal = new ArrayList<Text>();
    	for (ScriptElement sElem: members) {
    		retVal.addAll(sElem.getTextRuns());
    	}
    	
    	return retVal;
    }
    
    public String getTextContent() {
        if (members.size() == 0) return null;
        String textContent = "";
        
        for (ScriptElement run : members) {
            textContent += run.getTextContent();
        }
        
        return textContent;
    }
    
    public void accept(ISceneGroupVisitor visitor) {
		visitor.visit(this);
		for (ScriptElement elem: members) {
			elem.accept(visitor);
			visitor.visit(elem);
		}
	}
    
    public void accept(IElementGroupVisitor visitor) {
    	visitor.visit(this);
    	for (ScriptElement elem: members) {
			elem.accept(visitor);
		}
    }
    
    public void accept(IElementVisitor visitor) {
		for (ScriptElement elem: members) {
			elem.accept(visitor);
		}
	}   
    
    public void accept(ITextVisitor visitor) {
    	for (ScriptElement elem: members) {
			elem.accept(visitor);
		}
    }
   
    public void trimTextContent() {
		accept(new SceneGroupVisitor() {
			public void visit(ScriptElement elem) {
				elem.trimTextContent();
			}		
		});
		
		removeEmpties();
	}
	
	public void removeEmpties() {
    	ArrayList<ScriptElement> toRemove = new ArrayList<ScriptElement>();
		for (ScriptElement elem: members) {
			if (elem.getTextContent().trim().length() == 0) {
				toRemove.add(elem);
			}
		}
		
		for (ScriptElement removable : toRemove) {
			removeMember(removable);
		}
		
		accept(new SceneGroupVisitor() {
			public void visit(ScriptElement elem) {
				elem.removeEmpties();
			}	
		});
		
		for (ScriptElement elem : this.getMembers()) {
			if (elem.getTextContent().length() == 0) {
				removeMember(elem);
				removeEmpties();
				return;
			}
		}
	}
	
	public void normalize() {
		for (ScriptElement elem : members) {
			elem.normalize();
		}
	}
}
