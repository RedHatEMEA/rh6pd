package rh6pd.utils;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("rh6pd.utils.AgedValidator")
public class AgeValidator implements Validator {

	private int age;

	public int ageCheck(Date inputDOB) {

		Calendar dob = Calendar.getInstance();
		dob.setTime(inputDOB);
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		System.out.println("Initial age is: " + age);
		if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
			age--;
		} else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
				&& today.get(Calendar.DAY_OF_MONTH) < dob
						.get(Calendar.DAY_OF_MONTH)) {
			age--;
		}

		return age;
	}

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		Date inputDOB = (Date) value;

		if (inputDOB != null) {

			int driverAge = ageCheck(inputDOB);

			if (driverAge < 17) {
				FacesMessage msg = new FacesMessage("Driver Age Error",
						"Please enter an age over 17");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);

			} else if (driverAge > 100) {
				FacesMessage msg = new FacesMessage("Driver Age Error",
						"Too old to drive!");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}
	}
}
