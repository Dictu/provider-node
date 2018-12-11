package eu.toop.node.nl.kvk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

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
		JsonNode data = root.get("data");
		String kvkNumber = Constants.NOT_AVAILABLE;
		String businessName = Constants.NOT_AVAILABLE;
		String street = Constants.NOT_AVAILABLE;
		String postalCode = Constants.NOT_AVAILABLE;
		String city = Constants.NOT_AVAILABLE;
		String country = Constants.NOT_AVAILABLE;
		if (null != data) {
			JsonNode items = data.get("items");
			if (null != items) {
				Iterator<JsonNode> it = items.iterator();
				while (it.hasNext()) {
					JsonNode item = it.next();
					String isMainBranch = allOrNothing(item.get("isMainBranch"));
					if ("true".equals(isMainBranch)) {
						if (null != item) {
							kvkNumber = allOrNothing(item.get("kvkNumber"));
							JsonNode tradeNames = item.get("tradeNames");
							if (null != tradeNames) {
								businessName = allOrNothing(tradeNames.get("businessName"));
							}
							JsonNode addresses = item.get("addresses");
							if (null != addresses) {
								JsonNode address = addresses.get(0);
								if (null != address) {
									street = allOrNothing(address.get("street"));
									postalCode = allOrNothing(address.get("postalCode"));
									city = allOrNothing(address.get("city"));
									country = allOrNothing(address.get("country"));
								}
							}
						}
					}
				}
			}
		}
				
		set.setCompanyCode(kvkNumber);
		set.setCompanyName(businessName);
		set.setCompanyType(Constants.NOT_AVAILABLE);
		set.setLegalStatus(Constants.NOT_AVAILABLE);
		set.setLegalStatusEffectiveDate(Constants.NOT_AVAILABLE);
		set.setRegistrationAuthority("KamerVanKoophandel");
		set.setRegistrationDate(Constants.NOT_AVAILABLE);
		set.setRegistrationNumber(Constants.NOT_AVAILABLE);
		set.setActivityDeclaration(Constants.NOT_AVAILABLE);
		
		Address address = new Address();
		address.setStreetName(street);
		address.setPostalCode(postalCode);
		address.setCity(city);
		address.setCountry(country);
		set.setHeadOfficeAddres(address);
		return set;
	}
	
	public String allOrNothing(JsonNode node) {
		if (null != node) {
			return node.asText();
		}
		else {
			return Constants.NOT_AVAILABLE;
		}
	}
}
