import java.util.HashMap;
import java.util.Map;

import com.app.main.util.FTLString;
import com.app.task.Task;

public class FTLTest {

	public static void main(String[] args) {
		String s = "-qPatientID=@{task.patientid}|-qModality=@{task.modality}";
		s = s.replaceAll("@", "\\$");
		FTLString f = new FTLString();
		Map m = new HashMap<String, Object>();
		Task t = new Task();
		t.setPatientid("12345678");
		t.setModality("MRI");
		m.put("task", t);
		System.out.println(s);
		Task tt = (Task)m.get("task");
		System.out.println(tt.getPatientid()+":"+tt.getModality());
		String so = f.compileString(s, m);
		System.out.println(so);
		String a = "@";
		System.out.println(a.replaceAll("@", "\\$"));
	}
}
