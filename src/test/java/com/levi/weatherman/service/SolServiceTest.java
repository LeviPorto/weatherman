package com.levi.weatherman.service;

import com.levi.weatherman.WeathermanApplication;
import com.levi.weatherman.client.NasaRestTemplate;
import com.levi.weatherman.dto.AtDTO;
import com.levi.weatherman.dto.SolDTO;
import com.levi.weatherman.dto.SolTemperatureDTO;
import com.levi.weatherman.exception.SolDoesNotExistException;
import com.levi.weatherman.extractor.SolExtractor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeathermanApplication.class)
public class SolServiceTest {

    @InjectMocks
    private SolService service;

    @Mock
    private NasaRestTemplate nasaRestTemplate;

    @Mock
    private SolExtractor extractor;

    public static final String JSON_PARAMETER_AT = "AT";
    public static final String JSON_PARAMETER_AV = "av";
    public static final String JSON_PARAMETER_SOL_KEYS = "sol_keys";

    public static final double JSON_PARAMETER_FIRST_VALUE_AV = 20.0;
    public static final double JSON_PARAMETER_SECOND_VALUE_AV = 30.0;

    public static final String FIRST_SOL = "420";
    public static final String SECOND_SOL = "421";
    public static final String INVALID_SOL = "422";

    @Before
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void set_up() {
        BDDMockito.given(nasaRestTemplate.getSolsInformationFromNasa()).willReturn(new ResponseEntity(givenSolsInformationFromNasa(), HttpStatus.OK));
        BDDMockito.given(extractor.extractSol(givenSolsInformationFromNasa(), FIRST_SOL)).willReturn(givenFirstSol());
        BDDMockito.given(extractor.extractSol(givenSolsInformationFromNasa(), SECOND_SOL)).willReturn( givenSecondSol());
    }

    @Test
    public void it_should_return_all_sols_from_nasa() {
        Assert.assertEquals(service.findTemperaturesBySol(null), givenSolsTemperature());
    }

    @Test(expected = SolDoesNotExistException.class)
    public void it_should_return_exception_if_sent_sol_is_not_more_available() {
        Assert.assertEquals(service.findTemperaturesBySol(INVALID_SOL), givenSolsTemperature());
    }

    @Test
    public void it_should_return_specific_sol_from_nasa() {
        Assert.assertEquals(service.findTemperaturesBySol(FIRST_SOL), givenSolTemperature());
    }

    private LinkedHashMap<String, Object> givenSolsInformationFromNasa() {
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        LinkedHashMap<String, Object> firstSol = new LinkedHashMap<>();
        LinkedHashMap<String, Object> secondSol = new LinkedHashMap<>();
        LinkedHashMap<String, Double> firstAverageTemperature = new LinkedHashMap<>();
        LinkedHashMap<String, Double> secondAverageTemperature = new LinkedHashMap<>();
        ArrayList<String> solKeys = new ArrayList<>();
        solKeys.add(FIRST_SOL);
        solKeys.add(SECOND_SOL);
        firstAverageTemperature.put(JSON_PARAMETER_AV, JSON_PARAMETER_FIRST_VALUE_AV);
        secondAverageTemperature.put(JSON_PARAMETER_AV, JSON_PARAMETER_SECOND_VALUE_AV);
        firstSol.put(JSON_PARAMETER_AT, firstAverageTemperature);
        secondSol.put(JSON_PARAMETER_AT, secondAverageTemperature);
        body.put(FIRST_SOL, firstSol);
        body.put(SECOND_SOL, secondSol);
        body.put(JSON_PARAMETER_SOL_KEYS, solKeys);
        return body;
    }

    public SolDTO givenFirstSol() {
        SolDTO sol = new SolDTO();
        AtDTO at = new AtDTO();
        at.setAv(JSON_PARAMETER_FIRST_VALUE_AV);
        sol.setAtDTO(at);
        return sol;
    }

    public SolDTO givenSecondSol() {
        SolDTO sol = new SolDTO();
        AtDTO at = new AtDTO();
        at.setAv(JSON_PARAMETER_SECOND_VALUE_AV);
        sol.setAtDTO(at);
        return sol;
    }

    public List<SolTemperatureDTO> givenSolsTemperature() {
        return Arrays.asList(SolTemperatureDTO.builder().sol(FIRST_SOL).averageTemperature(JSON_PARAMETER_FIRST_VALUE_AV).build(),
                SolTemperatureDTO.builder().sol(SECOND_SOL).averageTemperature(JSON_PARAMETER_SECOND_VALUE_AV).build());
    }

    public List<SolTemperatureDTO> givenSolTemperature() {
        return Collections.singletonList(SolTemperatureDTO.builder()
                .sol(FIRST_SOL).averageTemperature(JSON_PARAMETER_FIRST_VALUE_AV).build());
    }

}
