package com.workshop.application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.application.datamodel.ComputePriceRequest;
import com.workshop.application.datamodel.OptionType;
import com.workshop.application.datamodel.PriceResponse;
import com.workshop.application.price.model.MaygardOptionPriceCalc;
import com.workshop.application.price.model.OptionPriceCalc;

@RestController
public class CMTWorkshopController {

	private final ExecutorService executor = Executors.newFixedThreadPool(10);
	DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("ddMMyyyy");
		
	
	@PostMapping("/cmt/workspace/calculate")
	public List<PriceResponse> calculatePrice(@RequestBody List<ComputePriceRequest> requestList) {
		CompletionService<PriceResponse> cs = new ExecutorCompletionService<>(executor);
		List<PriceResponse> responses = new ArrayList<>();
		requestList.forEach(R -> {
			cs.submit(getJob(R,OptionType.CALL));
			cs.submit(getJob(R,OptionType.PUT));
		});
		IntStream.range(0, requestList.size() * 2).forEach( i ->{
			PriceResponse r;
			try {
				r = cs.take().get();
				if(r != null)
					responses.add(r);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		});
		return responses;
	}
	
	private Callable<PriceResponse> getJob(ComputePriceRequest request, OptionType type){
		return new Callable<PriceResponse>() {
			
			@Override
			public PriceResponse call() throws Exception {
				OptionPriceCalc calc = new MaygardOptionPriceCalc(type);
				PriceResponse response = new PriceResponse(calc.getOptionPrice(request.getSpotPrice(),
						request.getStrikePrice(), request.getInterestRate()/100, 
						ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(request.getExpireDate(),formatter)),
						request.getInterestRate()/100),
						type, request.getStrikePrice(), request.getExpireDate());
				response.setRho(calc.getRho());
				response.setDelta(calc.getDelta());
				response.setGamma(calc.getGamma());
				response.setTheta(calc.getTheta());
				response.setVega(calc.getVega());
				return response;
			}
		};
	}
}
