/**
 * 
 */
package sg.com.prudential.esb;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

//import com.prudential.ivr.orchestration.controller.PolicyInfoController;
//import com.prudential.ivr.orchestration.service.PolicyService;

import sg.com.prudential.esb.ESBApiApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ESBApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ESBApiApplicationTest {

	@LocalServerPort
	int randomServerPort;

//	@Autowired
//	private DataSource dataSource;
//
//	@Autowired
//	private JdbcTemplate template;

//	@Autowired
//	private PolicyService policyService;
//
//	@Autowired
//	private PolicyInfoController controller;

//	@Value("${datasource.policynumber_basedSQL}")
//	private String policyNumberBaseSQL;
//
//	@Value("${datasource.nric_basedSQL}")
//	private String nricBasedSearchSQL;
//
//	@Value("${datasource.claimSQL}")
//	private String claimSQL;
//
//	final String NricSQLValue = "SELECT  AA.NATIONAL_ID_NUMBER NRIC,  CC.LIFE_SYSTEM_CONTRACT_NUMBER POLICY_NO,  "
//			+ "(SELECT CODE_VALUE FROM CORE.SYSTEM_CODE WHERE CODE_TYPE = 'CONTTYP' AND U_CODE_KEY = CC.CONTRACT_TYPE_CODE) POLICY_TYPE,    "
//			+ "(SELECT CODE_VALUE FROM CORE.SYSTEM_CODE WHERE CODE_TYPE = 'CONTRSTS' AND U_CODE_KEY = CC.CONTRACT_RISK_STATUS_CODE) POLICY_STATUS,   "
//			+ "(SELECT CODE_VALUE FROM CORE.SYSTEM_CODE WHERE CODE_TYPE = 'CONTPSTS' AND U_CODE_KEY = CC.CONTRACT_PREMIUM_STATUS_CODE) PREMIUM_STATUS,   "
//			+ "CC.PREMIUM_PAID_TO_DATE AS PREMIUM_PAID_DATE,   CC.SINGLE_PREMIUM AS SINGLE_PREMIUM,  CC.INSTALMENT_PREMIUM AS INSTALMENT_PREMIUM,  "
//			+ "(CASE  WHEN EE.POLICY_TYPE IN ('LINK', 'PAR_RP5') THEN DD.ESTIMATED_VALUE  WHEN EE.POLICY_TYPE IN ('LINK2') "
//			+ "THEN DD.ACC_ACCT_NON_GUAR_GROSS_SUR_VAL  WHEN EE.POLICY_TYPE IN ('NPAR') THEN DD.GROSS_SURRENDER_VALUE  WHEN EE.POLICY_TYPE IN ('NPAR_SP1')"
//			+ " THEN DD.GROSS_SUR_VAL_1 + DD.SURRENDER_PENALTY - DD.LOAN_AMOUNT - DD.LOAN_INTEREST   WHEN EE.POLICY_TYPE IN ('PAR_RP1', 'PAR_RP10', 'PAR_RP11',"
//			+ " 'PAR_RP2',  'PAR_RP3', 'PAR_RP4', 'PAR_RP6', 'PAR_RP7', 'PAR_RP8', 'PAR_RP9', 'PAR_SP1', 'PAR_SP2', 'PAR_SP3', 'PAR_SP4', 'PAR_SP5') "
//			+ "THEN DD.GROSS_SURRENDER_VALUE - DD.LOAN_AMOUNT - DD.LOAN_INTEREST - DD.AUTO_PREMIUM_LOAN_AMOUNT - DD.AUTO_PREMIUM_LOAN_INTEREST -"
//			+ " DD.SURGICAL_NURSING_LOAN_AMOUNT - DD.SURGICAL_NURSING_LOAN_INTEREST  ELSE NULL   END) AS SURRENDER_VALUE,   "
//			+ "DD.GROSS_LOAN_ALLOW NETT_POLICY_LOAN_VALUE,   (DD.GROSS_CASH_VALUE_REV_BONUS - DD.LOAN_AMOUNT - DD.LOAN_INTEREST - "
//			+ "DD.AUTO_PREMIUM_LOAN_AMOUNT - DD.AUTO_PREMIUM_LOAN_INTEREST - DD.SURGICAL_NURSING_LOAN_AMOUNT - DD.SURGICAL_NURSING_LOAN_INTEREST) "
//			+ "AS NETT_CASH_BONUS_VALUE ,  DD.CASHBACK_INTEREST + DD.TOTAL_CASHBACK   NETT_CASH_BENEFIT_VALUE   FROM  CORE.CLIENT AA,  "
//			+ "CORE.CLIENT_CONTRACT_ADVISER BB,  CORE.LIFE_CONTRACT CC,  CORE.LIFE_CONTRACT_VALUES DD ,   CORE.POLICY_TYPE_LOOKUP EE,  "
//			+ "CORE.SYSTEM_CODE FF   WHERE 1=1   AND FF.CODE_VALUE = EE.PLAN_CODE   AND FF.CODE_TYPE = 'CONTTYP'  AND FF.U_CODE_KEY = CC.CONTRACT_TYPE_CODE "
//			+ "AND DD.U_CONTRACT_KEY = CC.U_CONTRACT_KEY   AND (AA.U_CLIENT_KEY = BB.MAIN_OWNER_KEY OR AA.U_CLIENT_KEY = BB.JOINT_OWNER_KEY)   "
//			+ "AND BB.U_CONTRACT_KEY = CC.U_CONTRACT_KEY     AND AA.NATIONAL_ID_NUMBER = ?";
//
//	final String PolicyNumberSQLValue = "SELECT  AA.NATIONAL_ID_NUMBER NRIC,  CC.LIFE_SYSTEM_CONTRACT_NUMBER POLICY_NO,  "
//			+ "(SELECT CODE_VALUE FROM CORE.SYSTEM_CODE WHERE CODE_TYPE = 'CONTTYP' AND U_CODE_KEY = CC.CONTRACT_TYPE_CODE) POLICY_TYPE,"
//			+ "    (SELECT CODE_VALUE FROM CORE.SYSTEM_CODE WHERE CODE_TYPE = 'CONTRSTS' AND U_CODE_KEY = CC.CONTRACT_RISK_STATUS_CODE) POLICY_STATUS,"
//			+ "   (SELECT CODE_VALUE FROM CORE.SYSTEM_CODE WHERE CODE_TYPE = 'CONTPSTS' AND U_CODE_KEY = CC.CONTRACT_PREMIUM_STATUS_CODE) PREMIUM_STATUS,"
//			+ "   CC.PREMIUM_PAID_TO_DATE AS PREMIUM_PAID_DATE,   CC.SINGLE_PREMIUM AS SINGLE_PREMIUM,  CC.INSTALMENT_PREMIUM AS INSTALMENT_PREMIUM,"
//			+ "  (CASE  WHEN EE.POLICY_TYPE IN ('LINK', 'PAR_RP5') THEN DD.ESTIMATED_VALUE  WHEN EE.POLICY_TYPE IN ('LINK2') THEN "
//			+ "DD.ACC_ACCT_NON_GUAR_GROSS_SUR_VAL  WHEN EE.POLICY_TYPE IN ('NPAR') THEN DD.GROSS_SURRENDER_VALUE  WHEN EE.POLICY_TYPE IN ('NPAR_SP1')"
//			+ " THEN DD.GROSS_SUR_VAL_1 + DD.SURRENDER_PENALTY - DD.LOAN_AMOUNT - DD.LOAN_INTEREST   WHEN EE.POLICY_TYPE IN ('PAR_RP1', 'PAR_RP10', "
//			+ "'PAR_RP11', 'PAR_RP2',  'PAR_RP3', 'PAR_RP4', 'PAR_RP6', 'PAR_RP7', 'PAR_RP8', 'PAR_RP9', 'PAR_SP1', 'PAR_SP2', 'PAR_SP3', 'PAR_SP4', "
//			+ "'PAR_SP5') THEN DD.GROSS_SURRENDER_VALUE - DD.LOAN_AMOUNT - DD.LOAN_INTEREST - DD.AUTO_PREMIUM_LOAN_AMOUNT - DD.AUTO_PREMIUM_LOAN_INTEREST - "
//			+ "DD.SURGICAL_NURSING_LOAN_AMOUNT - DD.SURGICAL_NURSING_LOAN_INTEREST  ELSE NULL   END) AS SURRENDER_VALUE,   "
//			+ "DD.GROSS_LOAN_ALLOW NETT_POLICY_LOAN_VALUE,   (DD.GROSS_CASH_VALUE_REV_BONUS - DD.LOAN_AMOUNT - DD.LOAN_INTEREST - "
//			+ "DD.AUTO_PREMIUM_LOAN_AMOUNT - DD.AUTO_PREMIUM_LOAN_INTEREST - DD.SURGICAL_NURSING_LOAN_AMOUNT - DD.SURGICAL_NURSING_LOAN_INTEREST)"
//			+ " AS NETT_CASH_BONUS_VALUE ,  DD.CASHBACK_INTEREST + DD.TOTAL_CASHBACK   NETT_CASH_BENEFIT_VALUE   FROM  CORE.CLIENT AA,  "
//			+ "CORE.CLIENT_CONTRACT_ADVISER BB,  CORE.LIFE_CONTRACT CC,  CORE.LIFE_CONTRACT_VALUES DD ,   CORE.POLICY_TYPE_LOOKUP EE,  "
//			+ "CORE.SYSTEM_CODE FF   WHERE 1=1   AND FF.CODE_VALUE = EE.PLAN_CODE   AND FF.CODE_TYPE = 'CONTTYP'  AND FF.U_CODE_KEY = CC.CONTRACT_TYPE_CODE "
//			+ "AND DD.U_CONTRACT_KEY = CC.U_CONTRACT_KEY   AND (AA.U_CLIENT_KEY = BB.MAIN_OWNER_KEY OR AA.U_CLIENT_KEY = BB.JOINT_OWNER_KEY)   "
//			+ "AND BB.U_CONTRACT_KEY = CC.U_CONTRACT_KEY     AND CC.LIFE_SYSTEM_CONTRACT_NUMBER = ? ";
//
//	final String ClaimSQLValue = "SELECT  IFCHDR.CLAIM_STATUS AS CLAIM_STATUS, IFCHDR.HOSPITAL_REGISTRATION_"
//			+ "NUMBER AS HRN_NUMBER , IFCHDR.DATE_TIME_STAMP  FROM IFILECLAIMS.IFC_CLAIM_HEADER IFCHDR, "
//			+ "CORE.LIFE_CONTRACT CLC, CORE.CLIENT CC,   CORE.CLIENT_CONTRACT_ADVISER CCCA, CORE.SYSTEM_CODE "
//			+ "CSC WHERE CLC.LIFE_SYSTEM_CONTRACT_NUMBER = CONTRACT_NUMBER   AND CLC.CONTRACT_TYPE_CODE = "
//			+ "CSC.U_CODE_KEY  AND CLC.U_CONTRACT_KEY = CCCA.U_CONTRACT_KEY   AND CCCA.MAIN_LIFE_ASSURED_KEY = "
//			+ "CC.U_CLIENT_KEY AND HOSPITAL_REGISTRATION_NUMBER NOT IN    (SELECT ECLAIM_REF_NO FROM "
//			+ "IFILECLAIMS.ECLAIM_CLM_HEADER) AND CSC.CODE_DESCRIPTION = 'PruShield'   AND "
//			+ "IFCHDR.CONTRACT_NUMBER = ? ORDER BY DATE_TIME_STAMP desc FETCH FIRST 1 ROW ONLY  ";
//
	@Test
	public void ApplicationContextLoadTest() throws Exception {
	}
//
//	@Test
//	public void testDataSourceBean() {
//		assertThat(dataSource).isNotNull();
//	}
//
//	@Test
//	public void testJDBCTemplateBean() {
//		assertThat(template).isNotNull();
//	}
//
//	@Test
//	public void testPolicyServiceBean() {
//		assertThat(policyService).isNotNull();
//	}
//
//	@Test
//	public void testPolicyInfoControllerBean() {
//		assertThat(controller).isNotNull();
//	}
//
//	@Test
//	public void testNricBasedSearchSQL_Value() {
//		assertThat(nricBasedSearchSQL).isEqualTo(NricSQLValue);
//	}
//
//	@Test
//	public void testPolicyNumberBaseSQL_Value() {
//		assertThat(policyNumberBaseSQL).isEqualTo(PolicyNumberSQLValue);
//	}
//
//	@Test
//	public void testclaimSQL_Value() {
//		assertThat(claimSQL).isEqualTo(ClaimSQLValue);
//	}
//
//	@Test
//	public void test_ILP_LINK2_AND_LINK3_Policy_SurrenderValue_BonusValue_Check_success() throws URISyntaxException {
//		RestTemplate restTemplate = new RestTemplate();
//		final String baseUrl = "http://localhost:" + randomServerPort + "/policy-info/";
//		URI uri = new URI(baseUrl);
//		String inputJson = "{ \"appKey\" : \"MzWQ2UK6J5JhRtXA9Quy8zxMajI5R\", 	\"policyNumber\": \"99119127\" }";
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		HttpEntity<String> entity = new HttpEntity<String>(inputJson, headers);
//		ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);
//
//		// Verify request succeed
//		Assert.assertEquals(200, result.getStatusCodeValue());
//		String resultJson = "{\"status\":\"Success\",\"nric\":\"S8522621E\",\"policies\":[{"
//				+ "\"policyNumber\":\"99119127\",\"policyType\":\"SFA\",\"policyStatus\":\"IF\","
//				+ "\"premiumStatus\":\"FP\",\"premiumPaidToDate\":\"2019-09-01\","
//				+ "\"premiumAmt\":\"0.0\",\"surrenderValue\":\"441164.56\",\"policyLoan\":\"0.0\","
//				+ "\"cashBenefits\":\"0.0\",\"cashBonus\":\"0.0\",\"claims\":[]}]}";
//
//		assertThat(result.getBody().contentEquals("\"status\": \"Success\""));
//		assertThat(result.getBody().contentEquals("\"policyNumber\": \"99119127\""));
//		assertThat(result.getBody().contentEquals("\"surrenderValue\": \"441164.56\","));
//		assertThat(result.getBody().contentEquals("\"cashBonus\": \"0.0\","));
//		assertThat(result.getBody()).contains(resultJson);
//	}
//
//	@Test
//	public void test_ILP_LINK4_TypePolicy_SurrenderValue_BonusValue_Check_success() throws URISyntaxException
//	{
//	    RestTemplate restTemplate = new RestTemplate();
//	    final String baseUrl = "http://localhost:"+randomServerPort+"/policy-info/";
//	    URI uri = new URI(baseUrl);
//	    String inputJson ="{ \"appKey\" : \"MzWQ2UK6J5JhRtXA9Quy8zxMajI5R\", 	\"policyNumber\": \"99268424\" }";
//	    HttpHeaders headers = new HttpHeaders();
//	    headers.setContentType(MediaType.APPLICATION_JSON);
//	    HttpEntity<String> entity = new HttpEntity<String>(inputJson ,headers);
//	    ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);
//	     
//	    //Verify request succeed
//	    Assert.assertEquals(200, result.getStatusCodeValue());
//	    String resultJson = "{\"status\":\"Success\",\"nric\":\"S9119110E\",\"policies\":[{\"policyNumber\":\"99268424\",\"policyType\":\"PX2\","
//	    		+ "\"policyStatus\":\"IF\",\"premiumStatus\":\"PP\",\"premiumPaidToDate\":\"2019-09-01\",\"premiumAmt\":\"2400.0\","
//	    		+ "\"surrenderValue\":\"0.0\",\"policyLoan\":\"0.0\",\"cashBenefits\":\"0.0\",\"cashBonus\":\"0.0\",\"claims\":[]}]}";
//
//		assertThat(result.getBody().contentEquals("\"status\": \"Success\""));
//	    assertThat(result.getBody().contentEquals("\"policyNumber\": \"99268424\""));
//	    assertThat(result.getBody().contentEquals("\"surrenderValue\": \"0.0\","));
//	    assertThat(result.getBody().contentEquals("\"cashBonus\": \"0.0\","));
//	    assertThat(result.getBody()).contains(resultJson);
//	}
//
//	
//	@Test
//	public void test_Non_ILP_LINK4_TypePolicy_SurrenderValue_BonusValue_Check_success() throws URISyntaxException
//	{
//	    RestTemplate restTemplate = new RestTemplate();
//	    final String baseUrl = "http://localhost:"+randomServerPort+"/policy-info/";
//	    URI uri = new URI(baseUrl);
//	    String inputJson ="{ \"appKey\" : \"MzWQ2UK6J5JhRtXA9Quy8zxMajI5R\", 	\"policyNumber\": \"99161999\" }";
//	    HttpHeaders headers = new HttpHeaders();
//	    headers.setContentType(MediaType.APPLICATION_JSON);
//	    HttpEntity<String> entity = new HttpEntity<String>(inputJson ,headers);
//	    ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);
//	     
//	    //Verify request succeed
//	    Assert.assertEquals(200, result.getStatusCodeValue());
//	    String resultJson = "{\"status\":\"Success\",\"nric\":\"S9900000G\",\"policies\":[{\"policyNumber\":\"99161999\","
//	    		+ "\"policyType\":\"IR8\",\"policyStatus\":\"IF\",\"premiumStatus\":\"SP\",\"premiumPaidToDate\":\"2054-06-15\","
//	    		+ "\"premiumAmt\":\"0.0\",\"surrenderValue\":\"9834.18\",\"policyLoan\":\"0.0\",\"cashBenefits\":\"0.0\","
//	    		+ "\"cashBonus\":\"0.0\",\"claims\":[]}]}";
//
//		assertThat(result.getBody().contentEquals("\"status\": \"Success\""));
//	    assertThat(result.getBody().contentEquals("\"policyNumber\": \"99161999\""));
//	    assertThat(result.getBody().contentEquals("\"surrenderValue\": \"0.0\","));
//	    assertThat(result.getBody().contentEquals("\"cashBonus\": \"0.0\","));
//	    assertThat(result.getBody()).contains(resultJson);
//	}
//
//	
//	@Test
//	public void test_Non_ILP2_TypePolicy_SurrenderValue_BonusValue_Check_success() throws URISyntaxException
//	{
//	    RestTemplate restTemplate = new RestTemplate();
//	    final String baseUrl = "http://localhost:"+randomServerPort+"/policy-info/";
//	    URI uri = new URI(baseUrl);
//	    String inputJson ="{ \"appKey\" : \"MzWQ2UK6J5JhRtXA9Quy8zxMajI5R\", 	\"policyNumber\": \"99187473\" }";
//	    HttpHeaders headers = new HttpHeaders();
//	    headers.setContentType(MediaType.APPLICATION_JSON);
//	    HttpEntity<String> entity = new HttpEntity<String>(inputJson ,headers);
//	    ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);
//	     
//	    //Verify request succeed
//	    Assert.assertEquals(200, result.getStatusCodeValue());
//	    String resultJson = "{\"status\":\"Success\",\"nric\":\"S9871417J\",\"policies\":[{\"policyNumber\":\"99187473\","
//	    		+ "\"policyType\":\"EO7\",\"policyStatus\":\"IF\",\"premiumStatus\":\"PP\",\"premiumPaidToDate\":\"2032-01-18\","
//	    		+ "\"premiumAmt\":\"3007.6\",\"surrenderValue\":\"14792.78\",\"policyLoan\":\"13313.5\",\"cashBenefits\":\"1000.0\","
//	    		+ "\"cashBonus\":\"396.82\",\"claims\":[]}]}";
//
//		assertThat(result.getBody().contentEquals("\"status\": \"Success\""));
//	    assertThat(result.getBody().contentEquals("\"policyNumber\": \"99187473\""));
//	    assertThat(result.getBody().contentEquals("\"surrenderValue\": \"14792.78\","));
//	    assertThat(result.getBody().contentEquals("\"cashBonus\": \"396.82\","));
//	    assertThat(result.getBody()).contains(resultJson);
//	}

}
