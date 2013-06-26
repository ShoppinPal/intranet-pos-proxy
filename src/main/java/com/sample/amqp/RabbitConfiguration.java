/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sample.amqp;

import static java.lang.System.getenv;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Pulkit Singhal
 */
@Configuration
public class RabbitConfiguration {

	@Value("${default.exchange.request.queue.name}")
	private String requestQueueName;

	@Bean
    public ConnectionFactory connectionFactory() {
        final URI ampqUrl;
        try {
            ampqUrl = new URI(getEnvOrThrow("CLOUDAMQP_URL"));
        	//ampqUrl = new URI(getEnvOrThrow("RABBITMQ_BIGWIG_URL"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUsername(ampqUrl.getUserInfo().split(":")[0]);
        factory.setPassword(ampqUrl.getUserInfo().split(":")[1]);
        factory.setHost(ampqUrl.getHost());
        factory.setPort(ampqUrl.getPort());
        factory.setVirtualHost(ampqUrl.getPath().substring(1));

        return factory;
    }

    /**
     * The RabbitAdmin implementation does automatic lazy declaration of
     * Queues, Exchanges and Bindings declared in the same ApplicationContext.
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
    	RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
    	return rabbitAdmin;
    }

	/**
	 * We don't need to define any binding for this queue, since it's relying
	 * on the default (no-name) direct exchange to which every queue is implicitly bound.
	 * The AMQP specification defines this behavior.
	 */
    @Bean
    public Queue requestQueue() {
    	return new Queue(requestQueueName);
    }

	/**
     * http://static.springsource.org/spring-amqp/docs/1.2.x/reference/html/amqp.html#d4e383
     * Two implementations are available:
     * a) JsonMessageConverter which uses the org.codehaus.jackson 1.x library and
     * b) Jackson2JsonMessageConverter which uses the com.fasterxml.jackson 2.x library.
     * 
     * @return
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
    	return new JsonMessageConverter();
    }

    private static String getEnvOrThrow(String name) {
        final String env = getenv(name);
        if (env == null) {
            throw new IllegalStateException("Environment variable [" + name + "] is not set.");
        }
        return env;
    }
}
