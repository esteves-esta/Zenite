import React, { useState, useEffect } from 'react';
import api from '../../services/api';

import { Container } from './styles';
import DadosPessoais from './dadosPessoais';
import DadosEndereco from './dadosEndereco';
import DadosAcesso from './dadosAcesso';
import Loader from '../../components/Loader'

export default function CadastroFiscal(props) {

    const [validacaoSenha, setValidacaoSenha] = useState(false);
    const [pagina, setPagina] = useState(1);
    const [dados, setDados] = useState({});
    const caminho = props.match.path;
    const id = props.match.params.id;
    const isEdicao = caminho.includes("editar");
    const tipoPagina = isEdicao ? "Edição" : "Cadastro";
    
    const mudarPagina = isProximo => {
        isProximo ? setPagina(pagina+1) : setPagina(pagina-1);
    }

    const adicionarDados = novoDado => {
        setDados({ ...dados, ...novoDado });
    }

    useEffect(() => {
        async function consultarEdicao() {
          try {
            const token = localStorage.getItem("token");
            const response = await api.get(`/api/fiscal/${id}`, {
              headers: { Authorization: token },
            });
            const dadosConsulta = response.data;
            delete dadosConsulta["linhasId"];
            setDados({...dadosConsulta});
          } catch (e) {
            alert("Ocorreu um erro. Tente de novo.");
          }
        }
        if(isEdicao){
           consultarEdicao();
        }
    }, [id]);

    const cadastrar = async () => {
        const token = await localStorage.getItem('token');
        
        const response = await api.post('/api/fiscal', dados, {
            headers: {'Authorization': token}
          });
        
        if(response.status){
            props.history.push("/fiscal");
        }  
    }

    const editar = async () => {
        const token = await localStorage.getItem("token");
        try {
          if (validacaoSenha){
              const response = await api.put(`/api/fiscal/${id}`, dados, {
                headers: { Authorization: token },
              });
          
            if (response.status === 200) {
              props.history.push("/fiscal");
            } else {
              alert("Ocorreu um erro. Tente de novo");
            }
          }else {
              alert("Senha diferente, tente novamente.");
          }
    
        }catch(e) {
          alert("Ocorreu um erro. Tente de novo.");
        }
        
    };

    const chamada = () => {isEdicao ? editar() : cadastrar();}
    

    return (
        <Container>
            {
            isEdicao && Object.keys(dados).length === 0 ? (
                <Loader />
            ) : (

                pagina === 1 ?
                    <DadosPessoais mudarPagina={mudarPagina} tipoPagina={tipoPagina}
                        adicionarDados={adicionarDados} dados={dados} />
                : pagina === 2 ?
                    <DadosEndereco mudarPagina={mudarPagina} tipoPagina={tipoPagina} 
                        adicionarDados={adicionarDados} dados={dados} />
                : pagina === 3 ?
                    <DadosAcesso   mudarPagina={mudarPagina} tipoPagina={tipoPagina} 
                        adicionarDados={adicionarDados} dados={dados}
                        validarSenha={setValidacaoSenha} />
                : pagina === 4 ?
                    chamada()
                : <h1>Pagina não encontrada</h1>
            )}
        </Container>
    );
}
