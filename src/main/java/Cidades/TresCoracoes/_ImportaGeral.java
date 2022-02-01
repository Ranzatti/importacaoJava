package Cidades.TresCoracoes;


public class _ImportaGeral {

    public static void main(String[] args) {

        ImportaReceita importaReceita = new ImportaReceita();
        ImportaGuiaReceita importaGuiaReceita = new ImportaGuiaReceita();
        ImportaElemDespesa importaElemDespesa = new ImportaElemDespesa();
        ImportaEmpenhos importaEmpenhos = new ImportaEmpenhos();
        ImportaAnulacaoEmpenho importaAnulacaoEmpenho = new ImportaAnulacaoEmpenho();
        ImportaPagamento importaPagamento = new ImportaPagamento();


        for (int i = 2015; i <= 2021 ; i++) {

            importaReceita.init(i);
            importaGuiaReceita.init(i);

            //importaElemDespesa.init(i);
            //importaEmpenhos.init(i);
            //importaAnulacaoEmpenho.init(i);

            //importaOrdensPagto.init(i);

            //importaPagamento.init(i);

        }

    }

}
