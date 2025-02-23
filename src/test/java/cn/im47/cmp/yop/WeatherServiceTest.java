package cn.im47.cmp.yop;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

@SpringBootTest
class WeatherServiceTest {

    private WeatherService weatherService;

    /**
     * This method sets up the WeatherService instance for testing.
     *
     * @throws Exception If any error occurs during setup.
     */
    @BeforeEach
    void setUp() throws Exception {
        weatherService = new WeatherService();
    }

	@Disabled
	@Test
    void test1() {
		var stdioParams = ServerParameters.builder("java")
			.args("-jar",
					"target/yop-mcp-server-1.0.0-SNAPSHOT.jar")
			.build();

		var transport = new StdioClientTransport(stdioParams);
		var client = McpClient.sync(transport).build();

		client.initialize();

		// List and demonstrate tools
		ListToolsResult toolsList = client.listTools();
		System.out.println("Available Tools = " + toolsList);

		CallToolResult weatherForcastResult = client.callTool(new CallToolRequest("getWeatherForecastByLocation",
				Map.of("latitude", "47.6062", "longitude", "-122.3321")));
		System.out.println("Weather Forcast: " + weatherForcastResult);

		CallToolResult alertResult = client.callTool(new CallToolRequest("getAlerts", Map.of("state", "NY")));
		System.out.println("Alert Response = " + alertResult);

		client.closeGracefully();
	}

	@Test
    void testWeatherService() {
		WeatherService client = new WeatherService();
		System.out.println(client.getWeatherForecastByLocation(47.6062, -122.3321));
		System.out.println(client.getAlerts("NY"));
	}

    @Test
    void testGetWeatherForecastByLocation() {
        // Given
        double latitude = 47.6062;
        double longitude = -122.3321;

        // When
        String result = weatherService.getWeatherForecastByLocation(latitude, longitude);

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Temperature"));
        assertTrue(result.contains("Wind"));
        assertTrue(result.contains("Forecast"));
    }

    @Test
    void testGetAlerts() {
        // Given
        String state = "NY";

        // When
        String result = weatherService.getAlerts(state);

        // Then
        assertNotNull(result);
    }
}