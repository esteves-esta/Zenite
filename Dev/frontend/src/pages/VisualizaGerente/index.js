import React, { useEffect, useState } from "react";
import api from "../../services/api";

import { Container, Row } from "./styles";
import Tabela from "../../components/Tabela2";
import Paginacao from "../../components/Paginacao";
import Loader from "./../../components/Loader";
import CabecalhoConsulta from "../../components/CabecalhoConsulta";

export default function VisualizaGerente() {
  const [corpo, setCorpo] = useState([]);
  const [pagina, setPagina] = useState(0);
  const [total, setTotal] = useState(0);
  const [totalItens, setTotalItens] = useState(0);

  useEffect(() => {
    async function dadosCorpos() {
      const token = localStorage.getItem("token");

      const response = await api.get(`/api/gerente?pagina=${pagina}`, {
        headers: { Authorization: token },
      });
      let dados = response.data;
      setTotal(dados.totalPaginas);
      setTotalItens(dados.totalItens);
      let temp = [];
      dados.lista.forEach((item) => {
        temp.push(criaDados(item.id, item.nome, item.numeroTelefone, item.cpf));
      });
      setCorpo(temp);
    }
    dadosCorpos();
  }, [pagina]);

  function criaDados(id, nome, telefone, cpf) {
    return { id, nome, telefone, cpf };
  }

  return corpo.length <= 0 ? (
    <Loader />
  ) : (
    <Container>
      <CabecalhoConsulta
        botaoTitulo="Novo gerente"
        titulo="Gerente"
        url="gerente"
        totalItens={totalItens}
      />

      <Row>
        <Tabela dados={corpo} tipo="gerente" />
      </Row>

      <Row>
        <Paginacao
          pgAtual={pagina}
          totalPg={total}
          mudarPag={(p) => setPagina(p)}
          totalItens={totalItens}
        />
      </Row>
    </Container>
  );
}
