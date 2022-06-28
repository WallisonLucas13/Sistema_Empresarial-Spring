let custo = document.getElementById("custoServico");
let text = document.getElementById("text");
let input = document.getElementById("input");

if(input.innerText == ""){
    input.innerText = "0";
}
function calcular(){

   let valor = custo.value;
   let format = valor.substring(3);
   let final = parseInt(format) + parseInt(input.value);
   text.innerText = "O custo total será: R$ " + final;
}