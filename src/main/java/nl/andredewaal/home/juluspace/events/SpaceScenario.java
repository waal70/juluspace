/**
 * 
 */
package nl.andredewaal.home.juluspace.events;
import java.util.Collections;
import java.util.List;
/**
 * @author awaal
 *
 */
public final class SpaceScenario {
    private final String keyword;
    private final String name;
    private final String description;
    private final List<SpaceEvent> events;

	/**
	 * 
	 */
    public SpaceScenario(String name, String description) {
    	this.name = name;
    	this.description = description;
    	this.keyword = "Auto-populated";
    	this.events = null;
    }
	public SpaceScenario(String keyword, String name, String description, List<SpaceEvent> events) {
        this.keyword = keyword;
        this.name = name;
        this.description = description;
        this.events = Collections.unmodifiableList(events);
    }

    public String getName() {
        return name;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getDescription() {
        return description;
    }

    public List<SpaceEvent> getEvents() {
        return events;
    }
    public void add (SpaceEvent addSpaceEvent) {
    	this.events.add(addSpaceEvent);
    }

}
