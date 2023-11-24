package promitech.ecc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PeselTest {

    @ParameterizedTest
    @CsvSource({
            "1, INVALID",
            "1234567890a, INVALID",
            "98040477747, INVALID",
            "05252442983, VALID",
            "52052872490, VALID",
            "98040477748, VALID"
    })
    void should_validate_pesel(String pesel, Pesel.ValidationResult result) {
        assertThat(Pesel.Validator.validate(pesel)).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvSource({
            "80021576555, MALE",
            "01282019537, MALE",
            "01301846559, MALE",
            "55090441277, MALE",
            "64080477952, MALE",
            "54010385882, FEMALE",
            "88022977245, FEMALE",
            "02292654886, FEMALE",
            "99092061963, FEMALE",
            "68072986886, FEMALE"
    })
    void should_extract_sex(String pesel, Pesel.Sex sex) {
        assertThat(new Pesel(pesel).extractSex()).isEqualTo(sex);
    }

    @ParameterizedTest
    @CsvSource({
            "25082194649, 1925-08-21",
            "78060148149, 1978-06-01",
            "52052868523, 1952-05-28",
            "79230381483, 2079-03-03",
            "39271449261, 2039-07-14",
            "06241697182, 2006-04-16"
    })
    void should_extract_birth_date(String pesel, String dateStr) {
        assertThat(new Pesel(pesel).extractBirthDate()).isEqualTo(LocalDate.parse(dateStr));
    }

    @ParameterizedTest
    @CsvSource({
            "1925-08-21",
            "1978-06-01",
            "1952-05-28",
            "2079-03-03",
            "2039-07-14",
            "2006-04-16"
    })
    void shouldGenerateValidPesel(String dateStr) {
        // given
        LocalDate date = LocalDate.parse(dateStr);

        // when
        Pesel pesel = RandomPeselGenerator.nextPesel(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        // then
        assertThat(pesel.isValid()).describedAs("invalid pesel: " + pesel.getValue()).isTrue();
    }
}
