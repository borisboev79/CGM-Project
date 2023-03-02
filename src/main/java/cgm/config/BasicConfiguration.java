package cgm.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicConfiguration {

    @Bean
    public ModelMapper createModelMapper(){
        return new ModelMapper();
    }

}
