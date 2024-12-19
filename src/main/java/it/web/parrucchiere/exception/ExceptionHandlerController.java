package it.web.parrucchiere.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

@RestControllerAdvice
public class ExceptionHandlerController {

	@ExceptionHandler(BusinessException.class)
	public String handleBusinessException(HttpServletRequest request, BusinessException ex, Model model) {
	    // Aggiungi il messaggio di errore al modello
	    model.addAttribute("errorMessage", ex.getMessage());
	    
	    // Estrai il nome della vista dalla richiesta
	    String viewName = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
	    
	    // Se la vista è "form", reindirizza a "lista.html"
	    if ("aggiornaAppuntamentoForm".equals(viewName)) {
	        return "listaAppuntamenti";
	    }
	    
	    // Altrimenti, mantieni la stessa pagina
	    return viewName;
	}

}
