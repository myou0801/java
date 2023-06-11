package apbase.online;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApBaseOnlineSecurityProperties {

	private List<String> csrfIgnore = new ArrayList<>();

	private Headers headers = new Headers();

	public List<String> getCsrfIgnore() {
		return csrfIgnore;
	}

	public void setCsrfIgnore(List<String> csrfIgnore) {
		this.csrfIgnore = csrfIgnore;
	}

	public Headers getHeaders() {
		return headers;
	}

	public void setHeaders(Headers headers) {
		this.headers = headers;
	}

	public class Headers {

		private CSP csp = new CSP();

		public CSP getCsp() {
			return csp;
		}

		public void setCsp(CSP csp) {
			this.csp = csp;
		}

		public class CSP {
			private boolean reportOnly = false;

			private Map<String, List<String>> policyDirectives;

			public boolean isReportOnly() {
				return reportOnly;
			}

			public void setReportOnly(boolean reportOnly) {
				this.reportOnly = reportOnly;
			}

			public Map<String, List<String>> getPolicyDirectives() {
				return policyDirectives;
			}

			public void setPolicyDirectives(Map<String, List<String>> policyDirectives) {
				this.policyDirectives = policyDirectives;
			}
		}

	}

}
