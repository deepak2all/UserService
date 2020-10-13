package au.com.deepak.user.service.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

public class Utils {

	final static Logger logger = LogManager.getLogger(Utils.class);
	
	public static void printAllHeaders(MultiValueMap<String, String> headers) {
		headers.forEach((key, value) -> 
			logger.info(String.format("Header '%s' = %s",  key, String.join("|", value)))
		); 
		
	}
	
	public static void printAllRequestParams(MultiValueMap<String, String> allParams) {
		allParams.forEach((key, value) -> 
			logger.info(String.format("Params '%s' = %s",  key, String.join("|", value)))
		); 
		
	}
	
	public static <T> T bypassGetRequest(String baseUrl, String path, MultiValueMap<String, String> headers,
			MultiValueMap<String, String> allParams, Class<T> clazz) {
		WebClient webClient = buildWebClient(baseUrl, headers);
		
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap(allParams);
		
		return webClient
				.get()
				.uri(uriBuilder -> uriBuilder
						.path(path)
						.queryParams(queryParams)
						.build())
				.headers(httpHeaders ->
							httpHeaders.addAll(headers)
				)
				.retrieve()
				.bodyToMono(clazz)
				.block();
	}
	
	public static <T, B> T bypassPostRequest(String baseUrl, String path, MultiValueMap<String, String> headers,
			MultiValueMap<String, String> allParams, Class<T> clazz) {
		WebClient webClient = buildWebClient(baseUrl, headers);
		
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap(allParams);
		
		return webClient
				.post()
				.uri(uriBuilder -> uriBuilder
						.path(path)
						.queryParams(queryParams)
						.build())
				.headers(httpHeaders ->
							httpHeaders.addAll(headers)
				)
				.retrieve()
				.bodyToMono(clazz)
				.block();
	}

	private static WebClient buildWebClient(String baseUrl, MultiValueMap<String, String> headers) {
		// TODO Auto-generated method stub
		WebClient.Builder clientBuilder = WebClient
				.builder()
				.baseUrl(baseUrl);
		return clientBuilder.build();
	}
}
