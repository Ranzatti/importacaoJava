package Louveira;

public class _ImportaGeral {

    public static void main(String[] args) {

        ImportaReceita importaReceita = new ImportaReceita();
        ImportaGuiaReceita importaGuiaReceita = new ImportaGuiaReceita();
        ImportaAnulacaoReceita importaAnulacaoReceita = new ImportaAnulacaoReceita();
        ImportaElemDespesa importaElemDespesa = new ImportaElemDespesa();
        ImportaEmpenhos importaEmpenhos = new ImportaEmpenhos();
        ImportaAnulacaoEmpenho importaAnulacaoEmpenho = new ImportaAnulacaoEmpenho();
        ImportaOrdensPagto importaOrdensPagto = new ImportaOrdensPagto();
        ImportaRestosInscritos importaRestosInscritos = new ImportaRestosInscritos();
        ImportaPagamento importaPagamento = new ImportaPagamento();

        //importaRestosInscritos.init();

        for (int i = 2015; i <= 2021 ; i++) {

            //importaReceita.init(i);
            //importaGuiaReceita.init(i);
            //importaAnulacaoReceita.init(i);

            //importaElemDespesa.init(i);
            //importaEmpenhos.init(i);
            //importaAnulacaoEmpenho.init(i);

            //importaOrdensPagto.init(i);

            //importaPagamento.init(i);

        }

    }

}
