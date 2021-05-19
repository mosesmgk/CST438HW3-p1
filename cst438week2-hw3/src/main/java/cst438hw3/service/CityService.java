package cst438hw3.service;

import java.util.List;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import cst438hw3.domain.*;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange fanout;

    public CityService(CityRepository mockRepository, WeatherService mockWeather) {
        this.cityRepository = mockRepository;
        this.weatherService = mockWeather;
    }

   
    public ResponseEntity<CityInfo> getCityInfo(String cityName) {
        
        List<City> cities = cityRepository.findByName(cityName);
        if (cities.size() == 0) {

            return new ResponseEntity<CityInfo>(HttpStatus.NOT_FOUND);

        } else {
           
            City city = cities.get(0);

            TempAndTime weather = weatherService.getTempAndTime(city.getName());
            city.setWeather(weather);

            CityInfo cityInfo = new CityInfo(city);

            return new ResponseEntity<CityInfo>(cityInfo, HttpStatus.OK);
        }

    }

  
    public void requestReservation(String cityName, String level, String email) {

        String msg = "{\"cityName\": \"" + cityName + "\" \"level\": \"" + level
                + "\" \"email\": \"" + email + "\"}";
        System.out.println("Sending message:" + msg);
        rabbitTemplate.convertSendAndReceive(fanout.getName(), "", 
                msg);
    }
}
