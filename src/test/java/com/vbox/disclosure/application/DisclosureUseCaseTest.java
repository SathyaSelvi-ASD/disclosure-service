//package com.vbox.disclosure.application;
//
//import com.vbox.disclosure.api.dto.response.ApiResponse;
//import com.vbox.disclosure.config.WorkActionClientProperties;
//import com.vbox.disclosure.persistence.DisclosureRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.web.client.RestClient;
//import org.springframework.web.client.RestClientException;
//
//import java.io.ByteArrayInputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class DisclosureUseCaseTest {
//
//    @Mock
//    private DisclosureRepository repository;
//
//    @Mock
//    private RestClient workActionRestClient;
//
//    private DisclosureUseCase useCase;
//
//    @BeforeEach
//    void setUp() {
//        WorkActionClientProperties properties = new WorkActionClientProperties("http://localhost:8081", "/work-actions/search");
//        useCase = new DisclosureUseCase(repository, workActionRestClient, properties);
//    }
//
//    @Test
//    void shouldReturnBadGatewayWhenWorkActionCallFails() {
//        when(workActionRestClient.get()).thenThrow(new RestClientException("Work Action unavailable"));
//
//        ApiResponse response = useCase.searchWorkAction("WA-1001");
//
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
//        //assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
////        assertThat(response.getBody()).isNotNull();
////        assertThat(response.getBody().status()).isEqualTo("ERROR");
////        assertThat(response.getBody().statusCode()).isEqualTo(HttpStatus.BAD_GATEWAY.value());
////        assertThat(response.getBody().message()).isEqualTo("Work Action service is unavailable.");
//    }
//
//   // @SuppressWarnings({"rawtypes", "unchecked"})
////    @Test
////    void shouldMapSuccessResponseBodyToObjectForWorkActionSearch() {
////        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
////        RestClient.RequestHeadersSpec headersSpec = mock(RestClient.RequestHeadersSpec.class);
////
////        when(workActionRestClient.get()).thenReturn(uriSpec);
////        when(uriSpec.uri(any(Function.class))).thenReturn(headersSpec);
////        when(headersSpec.exchange(any())).thenAnswer(invocation -> {
////            RestClient.RequestHeadersSpec.ExchangeFunction<?> callback = invocation.getArgument(0);
////            RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse response = mock(RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse.class);
////            when(response.getStatusCode()).thenReturn(HttpStatus.ACCEPTED);
////            when(response.getBody()).thenReturn(new ByteArrayInputStream(
////                    "{\"work_action_referenceid\":\"WA-1001\"}".getBytes(StandardCharsets.UTF_8)));
////            return (ResponseEntity<ApiResponse>) callback.exchange(null, response);
////        });
////
////        ApiResponse response = useCase.searchWorkAction("WA-1001");
////
////        assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED);
////        assertThat(response).isNotNull();
////        assertThat(response.status()).isEqualTo("SUCCESS");
////        assertThat(response.data()).isEqualTo(Map.of("work_action_referenceid", "WA-1001"));
////    }
//}
