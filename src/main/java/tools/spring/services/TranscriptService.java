package tools.spring.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import tools.spring.domain.Transcript;


@Service
public class TranscriptService {
	
	public TranscriptService(){
		build();
	}
	
	public void build(){
		
	}
	
	public Transcript getTranscript(){
		return new Transcript();
	}

	public List<Transcript> getTranscriptList(){
		return new ArrayList<Transcript>();
	}
}