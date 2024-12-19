package it.web.parrucchiere.admin;

import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.web.parrucchiere.security.IUserRepository;
import it.web.parrucchiere.security.User;

@Controller
public class LoginController {

	private final IUserRepository userRepository;
	
	public LoginController(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/login")
	public String loginPage() {
		return "login"; // Assicurati che la vista "login" esista
	}

	@GetMapping("/indexAdmin")
    public String dashboard(@RequestParam("username") String username, @RequestParam("password") String password) {
        // Verifica se l'utente ha il ruolo "ROLE_ADMIN"
    	Optional<User> u = userRepository.findByUsernameByPassword(username, password);
        if (u.isPresent() && u.get().getRole().getName().equals("ADMIN")) {
            return "redirect:/admin/indexAdmin"; // Reindirizza all'area amministrativa
        }
        return "redirect:/"; // Altrimenti, reindirizza alla home page
    }

}