
package com.example.bibliotecaceub

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Livro(
    val id: Int,
    val titulo: String,
    val autor: String,
    val categoria: String,
    val isbn: String
)

data class EmprestimoComLivro(
    val id: Int,
    val tituloLivro: String,
    val estudante: String,
    val matricula: String,
    val dataEmprestimo: String,
    val dataDevolucao: String,
    val situacao: String
)

class BancoDados(context: Context) :
    SQLiteOpenHelper(context, "BibliotecaCEUB.db", null, 1) {

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE livros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                autor TEXT NOT NULL,
                categoria TEXT NOT NULL,
                isbn TEXT NOT NULL
            )
        """)

        db.execSQL("""
            CREATE TABLE emprestimos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                livro_id INTEGER NOT NULL,
                estudante TEXT NOT NULL,
                matricula TEXT NOT NULL,
                data_emprestimo TEXT NOT NULL,
                data_devolucao TEXT NOT NULL,
                situacao TEXT NOT NULL,
                FOREIGN KEY (livro_id) REFERENCES livros(id)
            )
        """)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS emprestimos")
        db.execSQL("DROP TABLE IF EXISTS livros")
        onCreate(db)
    }

    fun inserirLivro(
        titulo: String,
        autor: String,
        categoria: String,
        isbn: String
    ): Long {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("titulo", titulo)
            put("autor", autor)
            put("categoria", categoria)
            put("isbn", isbn)
        }

        return db.insert("livros", null, valores)
    }

    fun listarLivros(): List<Livro> {
        val lista = mutableListOf<Livro>()
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM livros", null)

        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    Livro(
                        id = it.getInt(it.getColumnIndexOrThrow("id")),
                        titulo = it.getString(it.getColumnIndexOrThrow("titulo")),
                        autor = it.getString(it.getColumnIndexOrThrow("autor")),
                        categoria = it.getString(it.getColumnIndexOrThrow("categoria")),
                        isbn = it.getString(it.getColumnIndexOrThrow("isbn"))
                    )
                )
            }
        }

        return lista
    }

    fun inserirEmprestimo(
        livroId: Int,
        estudante: String,
        matricula: String,
        dataEmprestimo: String,
        dataDevolucao: String,
        situacao: String
    ): Long {
        val db = writableDatabase

        val valores = ContentValues().apply {
            put("livro_id", livroId)
            put("estudante", estudante)
            put("matricula", matricula)
            put("data_emprestimo", dataEmprestimo)
            put("data_devolucao", dataDevolucao)
            put("situacao", situacao)
        }

        return db.insert("emprestimos", null, valores)
    }

    fun listarEmprestimosComLivros(): List<EmprestimoComLivro> {
        val lista = mutableListOf<EmprestimoComLivro>()
        val db = readableDatabase

        val sql = """
            SELECT
                emprestimos.id,
                livros.titulo,
                emprestimos.estudante,
                emprestimos.matricula,
                emprestimos.data_emprestimo,
                emprestimos.data_devolucao,
                emprestimos.situacao
            FROM emprestimos
            INNER JOIN livros
            ON emprestimos.livro_id = livros.id
        """

        val cursor = db.rawQuery(sql, null)

        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    EmprestimoComLivro(
                        id = it.getInt(0),
                        tituloLivro = it.getString(1),
                        estudante = it.getString(2),
                        matricula = it.getString(3),
                        dataEmprestimo = it.getString(4),
                        dataDevolucao = it.getString(5),
                        situacao = it.getString(6)
                    )
                )
            }
        }

        return lista
    }
}
