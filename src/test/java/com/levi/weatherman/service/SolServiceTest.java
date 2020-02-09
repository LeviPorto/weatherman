package com.levi.weatherman.service;

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
@SpringBootTest
@ActiveProfiles("test")
public class SolServiceTest {

    @InjectMocks
    private SolService service;

    @Mock
    private NasaRestTemplate nasaRestTemplate;

    @Mock
    private SolExtractor extractor;

    @Before
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void set_up() {
        BDDMockito.given(nasaRestTemplate.getSolsInformationFromNasa()).willReturn(new ResponseEntity(givenSolsInformationFromNasa(), HttpStatus.OK));
        BDDMockito.given(extractor.extractSol(givenSolsInformationFromNasa(), "420")).willReturn(givenFirstSol());
        BDDMockito.given(extractor.extractSol(givenSolsInformationFromNasa(), "421")).willReturn( givenSecondSol());
    }

    @Test
    public void it_should_return_all_sols_from_nasa() {
        Assert.assertEquals(service.findTemperaturesBySol(null), givenSolsTemperature());
    }

    @Test(expected = SolDoesNotExistException.class)
    public void it_should_return_exception_if_sent_sol_is_not_more_available() {
        Assert.assertEquals(service.findTemperaturesBySol("422"), givenSolsTemperature());
    }

    @Test
    public void it_should_return_specific_sol_from_nasa() {
        Assert.assertEquals(service.findTemperaturesBySol("420"), givenSolTemperature());
    }

    private LinkedHashMap<String, Object> givenSolsInformationFromNasa() {
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        LinkedHashMap<String, Object> firstSol = new LinkedHashMap<>();
        LinkedHashMap<String, Object> secondSol = new LinkedHashMap<>();
        LinkedHashMap<String, Double> firstAverageTemperature = new LinkedHashMap<>();
        LinkedHashMap<String, Double> secondAverageTemperature = new LinkedHashMap<>();
        ArrayList<String> solKeys = new ArrayList<>();
        solKeys.add("420");
        solKeys.add("421");
        firstAverageTemperature.put("av", 20.0);
        secondAverageTemperature.put("av", 30.0);
        firstSol.put("AT", firstAverageTemperature);
        secondSol.put("AT", secondAverageTemperature);
        body.put("420", firstSol);
        body.put("421", secondSol);
        body.put("sol_keys", solKeys);
        return body;
    }

    public SolDTO givenFirstSol() {
        SolDTO sol = new SolDTO();
        AtDTO at = new AtDTO();
        at.setAv(20.0);
        sol.setAtDTO(at);
        return sol;
    }

    public SolDTO givenSecondSol() {
        SolDTO sol = new SolDTO();
        AtDTO at = new AtDTO();
        at.setAv(30.0);
        sol.setAtDTO(at);
        return sol;
    }

    public List<SolTemperatureDTO> givenSolsTemperature() {
        return Arrays.asList(SolTemperatureDTO.builder().sol("420").averageTemperature(20.0).build(),
                SolTemperatureDTO.builder().sol("421").averageTemperature(30.0).build());
    }

    public List<SolTemperatureDTO> givenSolTemperature() {
        return Collections.singletonList(SolTemperatureDTO.builder().sol("420").averageTemperature(20.0).build());
    }

}
