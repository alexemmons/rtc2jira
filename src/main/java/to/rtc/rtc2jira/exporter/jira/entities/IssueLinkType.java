package to.rtc.rtc2jira.exporter.jira.entities;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonView;

@XmlRootElement
public class IssueLinkType extends NamedEntity {

  public final static IssueLinkType BLOCKS;
  public final static IssueLinkType CLONERS;
  public final static IssueLinkType DUPLICATE;
  public final static IssueLinkType RELATES;
  public final static IssueLinkType GIT_CODE_REVIEW;
  public final static IssueLinkType HIERARCHY;
  public final static IssueLinkType CATEGORY;
  public final static IssueLinkType ITERATION;
  public final static IssueLinkType STORY_TASKS;

  static {
    BLOCKS = new IssueLinkType("10000", "Blocks");
    CLONERS = new IssueLinkType("10001", "Cloners");
    DUPLICATE = new IssueLinkType("10002", "Duplicate");
    RELATES = new IssueLinkType("10003", "Relates");
    GIT_CODE_REVIEW = new IssueLinkType("10300", "Git Code Review");
    HIERARCHY = new IssueLinkType("10203", "Hierarchy");
    CATEGORY = new IssueLinkType("10201", "Category");
    ITERATION = new IssueLinkType("10204", "Iteration");
    STORY_TASKS = new IssueLinkType("10206", "Story Tasks");
  }

  String inward;
  String outward;

  public IssueLinkType() {}

  public IssueLinkType(String id, String name) {
    super(id, name);
  }

  @JsonView(IssueView.Update.class)
  @Override
  public String getKey() {
    // TODO Auto-generated method stub
    return super.getKey();
  }

  @Override
  public String getPath() {
    return "/issueLinkType";
  }

  public String getInward() {
    return inward;
  }

  public void setInward(String inward) {
    this.inward = inward;
  }

  public String getOutward() {
    return outward;
  }

  public void setOutward(String outward) {
    this.outward = outward;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    return prime * result + getName().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof IssueLinkType)) {
      return false;
    }
    IssueLinkType other = (IssueLinkType) obj;
    return other.getName().equals(this.getName());
  }

}
