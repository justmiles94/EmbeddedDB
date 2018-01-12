package tools.spring.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Extension {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	private static final long serialVersionUID = 1L;
	
	String Days = "", Instructor = "", Location = "", Attribute = "";
	Date Start, End, Begin, Finish;
	int Cap = 0, Act = 0, Rem = 0, WLCap = 0, WLAct = 0, WLRem = 0, XLCap = 0, XLAct = 0, XLRem = 0;
	
	public Extension() {}
	
	public String getDays() {
		return Days;
	}

	public void setDays(String days) {
		Days = days;
	}

	public String getInstructor() {
		return Instructor;
	}

	public void setInstructor(String instructor) {
		Instructor = instructor;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getAttribute() {
		return Attribute;
	}

	public void setAttribute(String attribute) {
		Attribute = attribute;
	}

	public Date getStart() {
		return Start;
	}
	public void setStart(Date date) {
		Start = date;
	}

	public Date getEnd() {
		return End;
	}

	public void setEnd(Date end) {
		End = end;
	}
	public Date getBegin() {
		return Begin;
	}
	public void setBegin(Date date) {
		Begin = date;
	}
	public Date getFinish() {
		return Finish;
	}
	public void setFinish(Date date) {
		Finish = date;
	}
	public int getCap() {
		return Cap;
	}

	public void setCap(int cap) {
		Cap = cap;
	}

	public int getAct() {
		return Act;
	}

	public void setAct(int act) {
		Act = act;
	}

	public int getRem() {
		return Rem;
	}

	public void setRem(int rem) {
		Rem = rem;
	}

	public int getWLCap() {
		return WLCap;
	}

	public void setWLCap(int wLCap) {
		WLCap = wLCap;
	}

	public int getWLAct() {
		return WLAct;
	}

	public void setWLAct(int wLAct) {
		WLAct = wLAct;
	}

	public int getWLRem() {
		return WLRem;
	}

	public void setWLRem(int wLRem) {
		WLRem = wLRem;
	}

	public int getXLCap() {
		return XLCap;
	}

	public void setXLCap(int xLCap) {
		XLCap = xLCap;
	}

	public int getXLAct() {
		return XLAct;
	}

	public void setXLAct(int xLAct) {
		XLAct = xLAct;
	}

	public int getXLRem() {
		return XLRem;
	}

	public void setXLRem(int xLRem) {
		XLRem = xLRem;
	}

	@Override
	public String toString() {
		return String.format(
				"CourseExtension [Days=%s, Instructor=%s, Location=%s, Attribute=%s, Start=%s, End=%s, Begin=%s, Finish=%s, Cap=%s, Act=%s, Rem=%s, WLCap=%s, WLAct=%s, WLRem=%s, XLCap=%s, XLAct=%s, XLRem=%s]",
				Days, Instructor, Location, Attribute, Start, End, Begin, Finish, Cap, Act, Rem, WLCap, WLAct, WLRem,
				XLCap, XLAct, XLRem);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Act;
		result = prime * result + ((Attribute == null) ? 0 : Attribute.hashCode());
		result = prime * result + ((Begin == null) ? 0 : Begin.hashCode());
		result = prime * result + Cap;
		result = prime * result + ((Days == null) ? 0 : Days.hashCode());
		result = prime * result + ((End == null) ? 0 : End.hashCode());
		result = prime * result + ((Finish == null) ? 0 : Finish.hashCode());
		result = prime * result + ((Instructor == null) ? 0 : Instructor.hashCode());
		result = prime * result + ((Location == null) ? 0 : Location.hashCode());
		result = prime * result + Rem;
		result = prime * result + ((Start == null) ? 0 : Start.hashCode());
		result = prime * result + WLAct;
		result = prime * result + WLCap;
		result = prime * result + WLRem;
		result = prime * result + XLAct;
		result = prime * result + XLCap;
		result = prime * result + XLRem;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Extension))
			return false;
		Extension other = (Extension) obj;
		if (Act != other.Act)
			return false;
		if (Attribute == null) {
			if (other.Attribute != null)
				return false;
		} else if (!Attribute.equals(other.Attribute))
			return false;
		if (Begin == null) {
			if (other.Begin != null)
				return false;
		} else if (!Begin.equals(other.Begin))
			return false;
		if (Cap != other.Cap)
			return false;
		if (Days == null) {
			if (other.Days != null)
				return false;
		} else if (!Days.equals(other.Days))
			return false;
		if (End == null) {
			if (other.End != null)
				return false;
		} else if (!End.equals(other.End))
			return false;
		if (Finish == null) {
			if (other.Finish != null)
				return false;
		} else if (!Finish.equals(other.Finish))
			return false;
		if (Instructor == null) {
			if (other.Instructor != null)
				return false;
		} else if (!Instructor.equals(other.Instructor))
			return false;
		if (Location == null) {
			if (other.Location != null)
				return false;
		} else if (!Location.equals(other.Location))
			return false;
		if (Rem != other.Rem)
			return false;
		if (Start == null) {
			if (other.Start != null)
				return false;
		} else if (!Start.equals(other.Start))
			return false;
		if (WLAct != other.WLAct)
			return false;
		if (WLCap != other.WLCap)
			return false;
		if (WLRem != other.WLRem)
			return false;
		if (XLAct != other.XLAct)
			return false;
		if (XLCap != other.XLCap)
			return false;
		if (XLRem != other.XLRem)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
