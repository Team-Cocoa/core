package kr.teamcocoa.core.test.utils;

import kr.teamcocoa.core.utils.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("StringUtils 테스트")
class StringUtilsTest {

    @Test
    @DisplayName("로마 숫자 변환")
    void 로마숫자_테스트() {
        assertEquals("I", StringUtils.toRoman(1));
        assertEquals("IV", StringUtils.toRoman(4));
        assertEquals("IX", StringUtils.toRoman(9));
        assertEquals("XIV", StringUtils.toRoman(14));
        assertEquals("XL", StringUtils.toRoman(40));
        assertEquals("MCMXCIV", StringUtils.toRoman(1994));
        assertEquals("MMMCMXCIX", StringUtils.toRoman(3999));
    }

    @Test
    @DisplayName("범위를 벗어난 로마 숫자는 예외")
    void 로마숫자_범위_테스트() {
        assertThrows(IllegalArgumentException.class, () -> StringUtils.toRoman(0));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.toRoman(4000));
    }

}
