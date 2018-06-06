# T2_ALEST2
Trabalho 2 - ALEST2 2018/1

Para compilar e executar os casos de teste, execute o script run.sh (Linux) ou run.bat (Windows). É necessário ter o Oracle JDK instalado.

Enunciado:

Sua empresa está desenvolvendo um complexo software de gerência de projetos que consegue
calcular o custo de projetos compostos por várias atividades, com custos diferentes e em quantidades
diferentes. O algoritmo para esta tarefa é sua responsabilidade e você deve ser capaz de
analisar um projeto e determinar seu custo final. Um projeto é composto por atividades com
custos próprios e uma atividade pode usar outras atividades: a atividade A pode custar $5 e
ainda usar 3 vezes a atividade B (que custa $4), com um custo final para A de $17.
A entrada tem o seguinte formato: a primeira linha informa k, que é o número de atividades existentes.
Depois seguem k linhas indicando o nome de cada atividade e seu custo próprio.
Em seguida vem l, o número de ligações entre as atividades, e mais l linhas
com os nomes de duas atividades p e q e um inteiro v, significando que a
atividade p usa a atividade q um total de v vezes.

Seu algoritmo deve apresentar o custo total do projeto. Você também sabe
que só existe um evento inicial (mas não custa muito testar isso no seu
algoritmo). A saída para cada caso de teste é composta por:
1. Identificação do caso de teste.
2. Resultado do caso de teste: custo total do projeto.
3. Tempo de execução do algoritmo.
