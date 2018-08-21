package eu.toop.node.nl.kvk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.toop.node.model.Address;
import eu.toop.node.model.ChamberOfCommerceDataSet;
import eu.toop.node.model.Constants;
import eu.toop.node.util.RestClient;

@Service
public class KvKProviderService extends RestClient {
	
	public KvKProviderService() {
		super();
	}

	public ChamberOfCommerceDataSet getDataSet(String kvk) throws IOException {
		ChamberOfCommerceDataSet set = new ChamberOfCommerceDataSet();
		String json = super.get("https://api.kvk.nl/api/v2/testsearch/companies?q=" + kvk);
		JsonNode root = new ObjectMapper().readTree(json.getBytes(StandardCharsets.UTF_8));
		JsonNode node = root.get("data").get("items").get(0);
		set.setCompanyCode(node.get("kvkNumber").asText());
		set.setCompanyName(node.get("tradeNames").get("businessName").asText());
		set.setCompanyType(Constants.NOT_AVAILABLE);
		set.setLegalStatus(Constants.NOT_AVAILABLE);
		set.setLegalStatusEffectiveDate(Constants.NOT_AVAILABLE);
		set.setRegistrationAuthority("KamerVanKoophandel");
		set.setRegistrationDate(Constants.NOT_AVAILABLE);
		set.setRegistrationNumber(Constants.NOT_AVAILABLE);
		set.setActivityDeclaration(Constants.NOT_AVAILABLE);
		
		Address address = new Address();
		address.setStreetName(node.get("addresses").get(0).get("street").asText());
		address.setPostalCode(node.get("addresses").get(0).get("postalCode").asText());
		address.setCity(node.get("addresses").get(0).get("city").asText());
		address.setCountry(node.get("addresses").get(0).get("country").asText());
		set.setHeadOfficeAddres(address);
		return set;
	}
}
