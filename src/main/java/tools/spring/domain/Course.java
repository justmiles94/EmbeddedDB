package tools.spring.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Course {
	
	private boolean Select = false;
	private String Subj = "", Crse = "", Sec = "", Cmp = "", Title = "";
	private double CredUpper = 0., CredLower = 0.;
	
	@Id
	@GeneratedValue
	private int CRN = 0;
	
	@Transient
	private List<String> prereqs = new ArrayList<String>();
	
	@Transient
	public List<Extension> extension = new ArrayList<Extension>();
	
	public Course() {}
	
	public Course(boolean select, String subj, String crse, String sec, String cmp, String title, double credUpper,
			double credLower, int cRN) {
		super();
		Select = select;
		Subj = subj;
		Crse = crse;
		Sec = sec;
		Cmp = cmp;
		Title = title;
		CredUpper = credUpper;
		CredLower = credLower;
		CRN = cRN;
	}

	public Course(boolean select, String subj, String crse, String sec, String cmp, String title, double credUpper,
			double credLower, int cRN, List<String> prereqs, List<Extension> extension) {
		super();
		Select = select;
		Subj = subj;
		Crse = crse;
		Sec = sec;
		Cmp = cmp;
		Title = title;
		CredUpper = credUpper;
		CredLower = credLower;
		CRN = cRN;
		this.prereqs = prereqs;
		this.extension = extension;
	}

	public boolean isSelect() {
		return Select;
	}

	public void setSelect(boolean select) {
		Select = select;
	}

	public String getSubj() {
		return Subj;
	}

	public void setSubj(String subj) {
		Subj = subj;
	}

	public String getCmp() {
		return Cmp;
	}

	public void setCmp(String cmp) {
		Cmp = cmp;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public double getCredUpper() {
		return CredUpper;
	}

	public void setCredUpper(double credUpper) {
		CredUpper = credUpper;
	}

	public double getCredLower() {
		return CredLower;
	}

	public void setCredLower(double credLower) {
		CredLower = credLower;
	}

	public int getCRN() {
		return CRN;
	}

	public void setCRN(int cRN) {
		CRN = cRN;
	}

	public String getCrse() {
		return Crse;
	}

	public void setCrse(String crse) {
		Crse = crse;
	}

	public String getSec() {
		return Sec;
	}

	public void setSec(String sec) {
		Sec = sec;
	}

	public List<Extension> getExtension() {
		return extension;
	}

	public void setExtension(Extension extension) {
		this.extension.add(extension);
	}

	public List<String> getPrereqs() {
		return prereqs;
	}

	public void setPrereqs(List<String> prereqs) {
		this.prereqs = prereqs;
	}

	@Override
	public String toString() {
		return String.format(
				"Course [Select=%s, Subj=%s, Crse=%s, Sec=%s, Cmp=%s, Title=%s, CredUpper=%s, CredLower=%s, CRN=%s, extension=%s, prereqs=%s]",
				Select, Subj, Crse, Sec, Cmp, Title, CredUpper, CredLower, CRN, extension, prereqs);
	}
	
}
