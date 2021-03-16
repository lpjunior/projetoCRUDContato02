package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Contato;
import entity.Endereco;
import service.ContatoService;

@WebServlet({ "/addcontato", "/edtcontato", "/listcontato", "/findcontato", "/delcontato" })
public class ContatoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private ContatoService service;
	
    public ContatoServlet() {
        super();
        service = new ContatoService();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getServletPath().equals("/listcontato")) {
			listaDeContatos(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getServletPath().equals("/addcontato")) {
			cadastrar(request, response);
		}
	}

	private void cadastrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(service.adicionarContato(new Contato(
			request.getParameter("inputNome"),
			request.getParameter("inputEmail"),
			request.getParameter("inputTelefone"),
			new Endereco(
				request.getParameter("inputLogradouro"),
				request.getParameter("inputCidade"),
				request.getParameter("inputEstado")
			)
		))) {
			// equivale ao echo do PHP
			response.getWriter().append("Dados gravados com sucesso.")
								.append("<html>")
								.append("<br>")
								.append("<a href='index.jsp'>Voltar</a>")
								.append("</html>");
		} else {
			response.getWriter().append("Falha ao gravar.")
								.append("<html>")
								.append("<br>")
								.append("<a href='index.jsp'>Voltar</a>")
								.append("</html>");
		}
	}
	
	private void listaDeContatos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			List<Contato> lista = service.lista();
			
			request.setAttribute("lista", lista);
			request.getRequestDispatcher("/listar.jsp").forward(request, response);
	}
}
