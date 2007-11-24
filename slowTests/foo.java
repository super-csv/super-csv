import java.io.FileReader;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.StrMinMax;
import org.supercsv.cellprocessor.constraint.Unique;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class foo {

	static final CellProcessor[]	userProcessors	= new CellProcessor[] { new Unique(new StrMinMax(5, 20)),
		new StrMinMax(8, 35), new ParseDate("dd/MM/yyyy"), new Optional(new ParseInt()), null };

	public static void main(String[] args) throws Exception {
		ICsvBeanReader inFile = new CsvBeanReader(new FileReader("foo.csv"), CsvPreference.EXCEL_PREFERENCE);
		try {
			final String[] header = inFile.getCSVHeader(true);
			UserBean user;
			while((user = inFile.read(UserBean.class, header, userProcessors)) != null) {
				System.out.println(user.getZip());
			}
		}
		finally {
			inFile.close();
		}
	}

	public static class UserBean {
		String	username, password, street, town;
		int		zip;

		public String getPassword() {
			return password;
		}

		public String getStreet() {
			return street;
		}

		public String getTown() {
			return town;
		}

		public String getUsername() {
			return username;
		}

		public int getZip() {
			return zip;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public void setTown(String town) {
			this.town = town;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public void setZip(int zip) {
			this.zip = zip;
		}
	}

}
