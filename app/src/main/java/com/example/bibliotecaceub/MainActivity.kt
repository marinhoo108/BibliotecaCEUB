
package com.example.bibliotecaceub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                BibliotecaApp()
            }
        }
    }
}

@Composable
fun BibliotecaApp() {
    var tela by remember { mutableStateOf("inicio") }

    when (tela) {
        "inicio" -> TelaInicial(
            abrirCadastro = { tela = "cadastro" },
            abrirConsulta = { tela = "consulta" },
            abrirEmprestimo = { tela = "emprestimo" },
            abrirConsultaEmprestimos = {
                tela = "consultaEmprestimos"
            }
        )

        "cadastro" -> TelaCadastroLivro(
            voltar = { tela = "inicio" }
        )

        "consulta" -> TelaConsultaLivros(
            voltar = { tela = "inicio" }
        )

        "emprestimo" -> TelaCadastroEmprestimo(
            voltar = { tela = "inicio" }
        )

        "consultaEmprestimos" -> TelaConsultaEmprestimos(
            voltar = { tela = "inicio" }
        )
    }
}

@Composable
fun TelaInicial(
    abrirCadastro: () -> Unit,
    abrirConsulta: () -> Unit,
    abrirEmprestimo: () -> Unit,
    abrirConsultaEmprestimos: () -> Unit
) {
    val azul = Color(0xFF245A91)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFEAF2FA)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "BIBLIOTECA CEUB",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = azul,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Organize livros e acompanhe empréstimos acadêmicos.",
                fontSize = 16.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            BotaoMenu("Cadastrar livros", azul, abrirCadastro)

            Spacer(modifier = Modifier.height(16.dp))

            BotaoMenu(
                "Registrar empréstimos",
                azul,
                abrirEmprestimo
            )

            Spacer(modifier = Modifier.height(16.dp))

            BotaoMenu(
                "Consultar empréstimos",
                azul,
                abrirConsultaEmprestimos
            )

            Spacer(modifier = Modifier.height(16.dp))

            BotaoMenu(
                "Consultar livros",
                azul,
                abrirConsulta
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Projeto acadêmico - Desenvolvimento Mobile",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun BotaoMenu(
    texto: String,
    cor: Color,
    acao: () -> Unit
) {
    Button(
        onClick = acao,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = cor
        )
    ) {
        Text(
            text = texto,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TelaCadastroLivro(voltar: () -> Unit) {
    val contexto = LocalContext.current
    val banco = remember { BancoDados(contexto) }

    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }

    val azul = Color(0xFF245A91)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF2FA))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cadastro de livros",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = azul
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título do livro") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = autor,
            onValueChange = { autor = it },
            label = { Text("Autor") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoria") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = isbn,
            onValueChange = { isbn = it },
            label = { Text("ISBN") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (
                    titulo.isBlank() ||
                    autor.isBlank() ||
                    categoria.isBlank() ||
                    isbn.isBlank()
                ) {
                    mensagem = "Preencha todos os campos!"
                } else {
                    val resultado = banco.inserirLivro(
                        titulo.trim(),
                        autor.trim(),
                        categoria.trim(),
                        isbn.trim()
                    )

                    if (resultado != -1L) {
                        mensagem = "Livro cadastrado com sucesso!"
                        titulo = ""
                        autor = ""
                        categoria = ""
                        isbn = ""
                    } else {
                        mensagem = "Não foi possível cadastrar o livro."
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = azul
            )
        ) {
            Text("Salvar livro", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (mensagem.isNotEmpty()) {
            Text(
                text = mensagem,
                color = azul,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = voltar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar ao início")
        }
    }
}

@Composable
fun TelaConsultaLivros(voltar: () -> Unit) {
    val contexto = LocalContext.current
    val banco = remember { BancoDados(contexto) }

    var livros by remember {
        mutableStateOf(banco.listarLivros())
    }

    val azul = Color(0xFF245A91)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF2FA))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Livros cadastrados",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = azul
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                livros = banco.listarLivros()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = azul
            )
        ) {
            Text("Atualizar lista")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (livros.isEmpty()) {
            Text("Nenhum livro cadastrado ainda.")
        } else {
            livros.forEach { livro ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = livro.titulo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = azul
                        )

                        Text("Autor: ${livro.autor}")
                        Text("Categoria: ${livro.categoria}")
                        Text("ISBN: ${livro.isbn}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = voltar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar ao início")
        }
    }
}

fun formatarData(valor: String): String {
    val numeros = valor.filter { it.isDigit() }.take(8)

    return when {
        numeros.length <= 2 -> numeros

        numeros.length <= 4 ->
            numeros.substring(0, 2) + "/" +
                    numeros.substring(2)

        else ->
            numeros.substring(0, 2) + "/" +
                    numeros.substring(2, 4) + "/" +
                    numeros.substring(4)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroEmprestimo(voltar: () -> Unit) {
    val contexto = LocalContext.current
    val banco = remember { BancoDados(contexto) }

    val livros = remember {
        banco.listarLivros()
    }

    var livroSelecionado by remember {
        mutableStateOf<Livro?>(null)
    }

    var estudante by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var dataEmprestimo by remember { mutableStateOf("") }
    var dataDevolucao by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }
    var expandido by remember { mutableStateOf(false) }

    val azul = Color(0xFF245A91)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF2FA))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registrar empréstimo",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = azul
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (livros.isEmpty()) {
            Text(
                text = "Cadastre pelo menos um livro antes de registrar um empréstimo.",
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        } else {
            ExposedDropdownMenuBox(
                expanded = expandido,
                onExpandedChange = {
                    expandido = !expandido
                }
            ) {
                OutlinedTextField(
                    value = livroSelecionado?.titulo ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Selecione o livro")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expandido
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandido,
                    onDismissRequest = {
                        expandido = false
                    }
                ) {
                    livros.forEach { livro ->
                        DropdownMenuItem(
                            text = {
                                Text(livro.titulo)
                            },
                            onClick = {
                                livroSelecionado = livro
                                mensagem = ""
                                expandido = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = estudante,
                onValueChange = {
                    estudante = it
                    mensagem = ""
                },
                label = { Text("Nome do estudante") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = matricula,
                onValueChange = {
                    matricula = it
                    mensagem = ""
                },
                label = { Text("Matrícula") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = dataEmprestimo,
                onValueChange = {
                    dataEmprestimo = formatarData(it)
                    mensagem = ""
                },
                label = {
                    Text("Data do empréstimo (DD/MM/AAAA)")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = dataDevolucao,
                onValueChange = {
                    dataDevolucao = formatarData(it)
                    mensagem = ""
                },
                label = {
                    Text("Data prevista de devolução (DD/MM/AAAA)")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val camposFaltando = mutableListOf<String>()

                    if (livroSelecionado == null || livroSelecionado!!.id <= 0) {
                        camposFaltando.add("selecione um livro")
                    }

                    if (estudante.isBlank()) {
                        camposFaltando.add("nome do estudante")
                    }

                    if (matricula.isBlank()) {
                        camposFaltando.add("matrícula")
                    }

                    if (dataEmprestimo.length != 10) {
                        camposFaltando.add("data completa do empréstimo")
                    }

                    if (dataDevolucao.length != 10) {
                        camposFaltando.add("data completa de devolução")
                    }

                    if (camposFaltando.isNotEmpty()) {
                        mensagem = "Confira: " +
                                camposFaltando.joinToString(", ") + "."
                    } else {
                        val livro = livroSelecionado

                        if (livro != null) {
                            val resultado = banco.inserirEmprestimo(
                                livro.id,
                                estudante.trim(),
                                matricula.trim(),
                                dataEmprestimo,
                                dataDevolucao,
                                "Pendente"
                            )

                            if (resultado != -1L) {
                                mensagem = "Empréstimo registrado com sucesso!"
                                livroSelecionado = null
                                estudante = ""
                                matricula = ""
                                dataEmprestimo = ""
                                dataDevolucao = ""
                            } else {
                                mensagem = "Não foi possível registrar o empréstimo."
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = azul
                )
            ) {
                Text("Salvar empréstimo")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (mensagem.isNotEmpty()) {
            Text(
                text = mensagem,
                color = azul,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = voltar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar ao início")
        }
    }
}

@Composable
fun TelaConsultaEmprestimos(voltar: () -> Unit) {
    val contexto = LocalContext.current
    val banco = remember { BancoDados(contexto) }

    var emprestimos by remember {
        mutableStateOf(banco.listarEmprestimosComLivros())
    }

    val azul = Color(0xFF245A91)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF2FA))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Empréstimos cadastrados",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = azul
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                emprestimos = banco.listarEmprestimosComLivros()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = azul
            )
        ) {
            Text("Atualizar lista")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (emprestimos.isEmpty()) {
            Text("Nenhum empréstimo cadastrado.")
        } else {
            emprestimos.forEach { emprestimo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = emprestimo.tituloLivro,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = azul
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Estudante: ${emprestimo.estudante}")
                        Text("Matrícula: ${emprestimo.matricula}")
                        Text("Data do empréstimo: ${emprestimo.dataEmprestimo}")
                        Text("Devolução prevista: ${emprestimo.dataDevolucao}")
                        Text("Situação: ${emprestimo.situacao}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = voltar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar ao início")
        }
    }
}
