package rh6pd.businessRules;

import org.jbpm.evaluation.carinsurance.IniFile;
import org.junit.Assert;
import org.junit.Test;

public class TestIniFile {
	@Test
	public void testFirst() throws Exception {
		IniFile i = new IniFile();
		i.open("//home//jread//testFile.ini");

		i.putInt("test", 1);

		Assert.assertEquals(1, i.getInt("test"));

		i.save();

		i.load();

		Assert.assertEquals(1, i.getInt("test"));
	}
}
