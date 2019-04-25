package com.workshop.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.math3.util.Precision;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.application.datamodel.ComputePriceRequest;
import com.workshop.application.datamodel.OptionPriceRequest;
import com.workshop.application.datamodel.OptionType;
import com.workshop.application.datamodel.PriceComparisonResponse;
import com.workshop.application.datamodel.PriceResponse;
import com.workshop.application.price.model.OptionPriceCalc;
import com.workshop.application.price.model.PriceModelFactory;

@RestController
public class CMTWorkshopController {

	private final ExecutorService executor = Executors.newFixedThreadPool(10);
	private CompletionService<PriceResponse> cs = new ExecutorCompletionService<>(executor);

	@CrossOrigin
	@GetMapping("/")
	public String index() {
		return "Welcome to Option Pricing calculator services";
	}
	
	@CrossOrigin
	@PostMapping("/cmt/workspace/calculate")
	public List<PriceResponse> calculatePrice(@RequestBody ComputePriceRequest request) {
		List<PriceResponse> responses = new ArrayList<>();
		List<Future<PriceResponse>> futures = new ArrayList<>();
		request.getRequests().forEach(R -> {
			futures.add(cs.submit(getJob(R, request.getModel(), request.getSpotPrice(), request.getInterestRate())));
		});
		futures.forEach( F ->{
			PriceResponse r;
			try {
				r = F.get();
				if(r != null)
					responses.add(r);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		});
		return responses;
	}
	
	@CrossOrigin
	@PostMapping("/cmt/workspace/compare")
	public List<PriceComparisonResponse> compareModels(@RequestBody ComputePriceRequest request) {
		List<PriceComparisonResponse> responses = new ArrayList<>();
		Future<PriceResponse> maygaurd;
		Future<PriceResponse> jquant;
			
		//for (OptionType type : OptionType.values()) {
			for (OptionPriceRequest r : request.getRequests()) {
				//r.setOptionType(type.name());
				OptionType type = r.getOptionType();

				maygaurd = cs.submit(getJob(r, "mayguard", request.getSpotPrice(), request.getInterestRate()));
				jquant = cs.submit(getJob(r, "jquant", request.getSpotPrice(), request.getInterestRate()));
				PriceComparisonResponse res = new PriceComparisonResponse(r.getStrikePrice(), r.getExpireDate(), type);
				try {
					res.setPriceMG(maygaurd.get() != null ? maygaurd.get().getPrice():0.0d);
					res.setPriceJquant(jquant.get()!= null ? jquant.get().getPrice():0.0d);
					responses.add(res);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		//}
		return responses;
	}
	
	
	private Callable<PriceResponse> getJob(OptionPriceRequest request, String model, double spotPrice, double interestRate){
		return new Callable<PriceResponse>() {
			
			@Override
			public PriceResponse call() throws Exception {
				OptionPriceCalc calc = PriceModelFactory.getModel(model, request.getOptionType());
				PriceResponse response = new PriceResponse(calc.getOptionPrice(spotPrice,
						request.getStrikePrice(), request.getImpliedVolatility()/100, 
						request.getExpireDate(),
						interestRate/100),
						request.getOptionType(), request.getStrikePrice(), request.getExpireDate());
				response.setRho(Precision.round(calc.getRho(),4));
				response.setDelta(Precision.round(calc.getDelta(),4));
				response.setGamma(Precision.round(calc.getGamma(),4));
				response.setTheta(Precision.round(calc.getTheta(),4));
				response.setVega(Precision.round(calc.getVega(),4));
				return response;
			}
		};
	}
}
