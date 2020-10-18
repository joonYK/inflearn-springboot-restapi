package jy.learning.bootrestapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development with Spring")
                .build();

        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        //Given
        String name = "Event";
        String description = "Spring";

        //When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        //Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @ParameterizedTest(name = "{index} => basePrice={0}, maxPrice={1}, isFree={2}")
    @MethodSource("paramsForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        //Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private static Object[] paramsForTestFree() {
        return new Object[] {
            new Object[] {0, 0, true},
            new Object[] {100, 0, false},
            new Object[] {0, 100, false},
            new Object[] {0, 100, false}
        };
    }

    @ParameterizedTest(name = "{index} => location={0}, isOffline={1}")
    @MethodSource("parametersForTestOffline")
    public void testOffline(String location, boolean isOffline) {
        //Given
        Event event = Event.builder()
                .location(location)
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private static Object[] parametersForTestOffline() {
        return new Object[] {
            new Object[] {"강남", true},
            new Object[] {null, false},
            new Object[] {"     ", false},
        };
    }
}