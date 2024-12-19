/**
 * 
 */

function toggleSubmenu(id) {
	var submenu = document.getElementById(id);
	if (submenu.classList.contains('hidden')) {
		submenu.classList.remove('hidden');
	} else {
		submenu.classList.add('hidden');
	}
}


function ricercaClienti(suffisso) {
	var input = document.getElementById('searchInput_' + suffisso);
	var suggestionsContainer = document.getElementById('suggestionsContainer_' + suffisso);
	var hiddenInput = document.getElementById('clienteId_' + suffisso);
	var query = input.value;

	// Nascondi la tendina se l'input è vuoto
	if (query.trim() === '') {
		suggestionsContainer.classList.add('hidden');
		suggestionsContainer.innerHTML = ''; // Pulisci i suggerimenti
		return;
	}

	// Effettua una richiesta al server per ottenere i suggerimenti
	fetch('/api/cliente/suggerimenti?query=' + encodeURIComponent(query))
		.then(response => response.json())
		.then(data => {
			suggestionsContainer.innerHTML = ''; // Pulisci i suggerimenti precedenti

			// Popola la tendina con i nuovi suggerimenti
			data.forEach(cliente => {
				var suggestionItem = document.createElement('div');
				suggestionItem.textContent = cliente.nome + ' ' + cliente.cognome + ' - ' + cliente.telefono;
				suggestionItem.classList.add('px-4', 'py-2', 'cursor-pointer', 'hover:bg-gray-100');
				suggestionItem.onclick = function() {
					input.value = cliente.nome + ' ' + cliente.cognome + ' - ' + cliente.telefono;
					hiddenInput.value = cliente.id; // Aggiorna solo il campo nascosto associato
					input.focus(); // Mantieni il focus sull'input
					suggestionsContainer.classList.add('hidden'); // Nascondi la tendina
				};
				suggestionsContainer.appendChild(suggestionItem);
			});

			// Mostra la tendina se ci sono suggerimenti
			if (data.length > 0) {
				suggestionsContainer.classList.remove('hidden');
				suggestionsContainer.classList.add('bg-white', 'text-black');
			} else {
				suggestionsContainer.classList.add('hidden');
			}
		})
		.catch(error => console.error('Errore nella richiesta dei suggerimenti:', error));
}

// Funzione per pre-popolare il cliente in modalità aggiornamento
function prePopulateCliente(suffisso) {
	// First, try to get 'vengo' from the hidden input
	const vengoInput = document.getElementById('vengoMode');
	const vengo = vengoInput ? vengoInput.value :
		new URLSearchParams(window.location.search).get('vengo');

	console.log('Vengo:', vengo); // Debug log

	if (vengo === 'aggiorna') {
		const existingClienteId = document.getElementById('clienteId_' + suffisso).value;

		console.log('Cliente ID:', existingClienteId); // Debug log

		if (existingClienteId) {
			fetch('/api/cliente/dettagli?id=' + encodeURIComponent(existingClienteId))
				.then(response => {
					console.log('Response status:', response.status);
					return response.json();
				})
				.then(cliente => {
					console.log('Cliente details:', cliente);

					const input = document.getElementById('searchInput_' + suffisso);
					const hiddenInput = document.getElementById('clienteId_' + suffisso);
					const suggestionsContainer = document.getElementById('suggestionsContainer_' + suffisso);

					input.value = cliente.nome + ' ' + cliente.cognome + ' - ' + cliente.telefono;
					hiddenInput.value = cliente.id;

					suggestionsContainer.classList.add('hidden');
				})
				.catch(error => console.error('Errore nel recuperare i dettagli del cliente:', error));
		}
	}
}

document.addEventListener('DOMContentLoaded', () => {
	prePopulateCliente('formAppuntamento');
});

function ricercaOperatori(suffisso) {
	var input = document.getElementById('searchInput1_' + suffisso);
	var suggestionsContainer = document.getElementById('suggestionsContainer1_' + suffisso);
	var hiddenInput = document.getElementById('operatoreId_' + suffisso);
	var query = input.value;

	// Nascondi la tendina se l'input è vuoto
	if (query.trim() === '') {
		suggestionsContainer.classList.add('hidden');
		suggestionsContainer.innerHTML = ''; // Pulisci i suggerimenti
		return;
	}

	// Effettua una richiesta al server per ottenere i suggerimenti
	fetch('/api/operatore/suggerimenti?query=' + encodeURIComponent(query))
		.then(response => response.json())
		.then(data => {
			suggestionsContainer.innerHTML = ''; // Pulisci i suggerimenti precedenti

			// Popola la tendina con i nuovi suggerimenti
			data.forEach(operatore => {
				var suggestionItem = document.createElement('div');
				suggestionItem.textContent = operatore.nome + ' ' + operatore.cognome;
				suggestionItem.classList.add('px-4', 'py-2', 'cursor-pointer', 'hover:bg-gray-100');
				suggestionItem.onclick = function() {
					input.value = operatore.nome + ' ' + operatore.cognome;
					hiddenInput.value = operatore.id; // Aggiorna solo il campo nascosto associato
					input.focus(); // Mantieni il focus sull'input
					suggestionsContainer.classList.add('hidden'); // Nascondi la tendina
				};
				suggestionsContainer.appendChild(suggestionItem);
			});

			// Mostra la tendina se ci sono suggerimenti
			if (data.length > 0) {
				suggestionsContainer.classList.remove('hidden');
				suggestionsContainer.classList.add('bg-white', 'text-black');
			} else {
				suggestionsContainer.classList.add('hidden');
			}
		})
		.catch(error => console.error('Errore nella richiesta dei suggerimenti:', error));
}

// Funzione per pre-popolare il cliente in modalità aggiornamento
function prePopulateOperatore(suffisso) {
	// First, try to get 'vengo' from the hidden input
	const vengoInput = document.getElementById('vengoMode1');
	const vengo = vengoInput ? vengoInput.value :
		new URLSearchParams(window.location.search).get('vengo');

	console.log('Vengo:', vengo); // Debug log

	if (vengo === 'aggiorna') {
		const existingOperatoreId = document.getElementById('operatoreId_' + suffisso).value;

		console.log('Operatore ID:', existingOperatoreId); // Debug log

		if (existingOperatoreId) {
			fetch('/api/operatore/dettagli?id=' + encodeURIComponent(existingOperatoreId))
				.then(response => {
					console.log('Response status:', response.status);
					return response.json();
				})
				.then(operatore => {
					console.log('Operatore details:', operatore);

					const input = document.getElementById('searchInput1_' + suffisso);
					const hiddenInput = document.getElementById('operatoreId_' + suffisso);
					const suggestionsContainer = document.getElementById('suggestionsContainer1_' + suffisso);

					input.value = operatore.nome + ' ' + operatore.cognome;
					hiddenInput.value = operatore.id;

					suggestionsContainer.classList.add('hidden');
				})
				.catch(error => console.error('Errore nel recuperare i dettagli del operatore:', error));
		}
	}
}

document.addEventListener('DOMContentLoaded', () => {
	prePopulateOperatore('formAppuntamento');
});

function bloccaInvioForm() {
	const form = document.getElementById('appuntamentoForm');
	const selectOra = document.getElementById('ora');

	if (form) {
		form.addEventListener('submit', function(event) {
			if (selectOra.value === '') {
				event.preventDefault();
				alert('Seleziona un orario valido prima di inviare il form.');
				return;
			}

			const checkboxes = document.querySelectorAll('input[type="checkbox"][name="servizi"]:checked');
			if (checkboxes.length === 0) {
				event.preventDefault();
				alert('Seleziona almeno un servizio.');
				return;
			}

		});
	} else {
		console.error('Form non trovato');
	}
}
document.addEventListener('DOMContentLoaded', bloccaInvioForm);

function calcolaTotale() {
    let totale = 0;  // Inizializziamo il totale a zero
    let sconto = parseFloat(document.getElementById('sconto').value) || 0;  // Otteniamo lo sconto inserito

    // Seleziona tutti i checkbox dei servizi
    let serviziSelezionati = document.querySelectorAll('input[type="checkbox"]:checked');

    // Calcola la somma dei prezzi dei servizi selezionati
    serviziSelezionati.forEach(function(servizio) {
        let prezzo = parseFloat(servizio.getAttribute('data-prezzo')); // Ottieni il prezzo dal dato 'data-prezzo'
        totale += prezzo;  // Somma il prezzo al totale
    });

    // Applica lo sconto al totale
    totale = totale - sconto;

    // Mostra il totale aggiornato nel campo di input
    document.getElementById('totale').value = totale.toFixed(2);  // Mostra il totale con due decimali
}

function aggiungiRequired(inputId) {
    const input = document.getElementById(inputId);
    if (input) {
        input.setAttribute('required', 'required');
    }
}