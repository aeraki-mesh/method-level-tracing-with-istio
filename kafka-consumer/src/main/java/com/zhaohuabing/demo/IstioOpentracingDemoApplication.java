package com.zhaohuabing.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import brave.Tracing;
import brave.opentracing.BraveTracer;
import brave.sampler.Sampler;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;
import zipkin2.reporter.Reporter;
import  brave.propagation.B3Propagation;

/**
 * Huabing Zhao
 */
@SpringBootApplication
public class IstioOpentracingDemoApplication {
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}


	@Bean
	public io.opentracing.Tracer zipkinTracer() {
		String zipkinEndpoint = System.getenv("ZIPKIN_ENDPOINT");
		if (zipkinEndpoint == null || zipkinEndpoint == ""){
			zipkinEndpoint = "http://zipkin.istio-system:9411/api/v2/spans";
		}

		OkHttpSender sender = OkHttpSender.create(zipkinEndpoint);
		Reporter spanReporter = AsyncReporter.create(sender);


		Tracing braveTracing = Tracing.newBuilder()
				.localServiceName("my-service")
				.propagationFactory(B3Propagation.FACTORY)
				.spanReporter(spanReporter)
				.build();

		Tracing braveTracer = Tracing.newBuilder()
				.localServiceName("spring-boot")
				.spanReporter(spanReporter)
				.propagationFactory(B3Propagation.FACTORY)
				.traceId128Bit(true)
				.sampler(Sampler.ALWAYS_SAMPLE)
				.build();
		return BraveTracer.create(braveTracer);
	}

	public static void main(String[] args) {
		SpringApplication.run(IstioOpentracingDemoApplication.class, args);
	}
}
