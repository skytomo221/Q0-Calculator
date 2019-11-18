# Q0 Calculator

<img src="https://github.com/skytomo221/Q0-Calculator/blob/master/images/icon.png?raw=true" width="200px">

**Q0 Calculator** は Java の課題のために作られた電卓プログラムです。  
そのため、単位が確定するまでは、このソースコードの利用をお控えください。

## 現在対応済みの型

|型名|説明|
|:-:|:-:|
|`INT32`|整数型|
|`Double`|浮動小数点型|
|`Boolean`|真理値|

## 演算子の優先順位

|優先順位|演算子|
|:-:|:-:|
|6|`()`|
|5|`*` `/` `&` `%`|
|4|`==` `!=` `<=` `<` `>` `>=`|
|3|`&&`|
|2|`||`|
|1|`=`|

## 現在対応済みの演算子

### 2項演算子

|OP1|OP|OP2|結果|説明|
|:-:|:-:|:-:|:-:|:-:|
|`Double`|`+`|`Double`|`Double`|加算|
|`Double`|`-`|`Double`|`Double`|減算|
|`Double`|`*`|`Double`|`Double`|乗算|
|`Double`|`×`|`Double`|`Double`|乗算|
|`Double`|`/`|`Double`|`Double`|除算|
|`Double`|`÷`|`Double`|`Double`|除算|
|`Double`|`+`|`Double`|`Double`|加算|
|`Double`|`-`|`Double`|`Double`|減算|
|`Double`|`*`|`Double`|`Double`|乗算|
|`Double`|`×`|`Double`|`Double`|乗算|
|`Double`|`/`|`Double`|`Double`|除算|
|`Double`|`÷`|`Double`|`Double`|除算|
|`INT32`|`÷`|`INT32`|`Double`|除算|
|`INT32`|`+`|`Double`|`Double`|加算|
|`INT32`|`-`|`Double`|`Double`|減算|
|`INT32`|`*`|`Double`|`Double`|乗算|
|`INT32`|`×`|`Double`|`Double`|乗算|
|`INT32`|`/`|`Double`|`Double`|除算|
|`INT32`|`÷`|`Double`|`Double`|除算|
|`INT32`|`+`|`INT32`|`INT32`|加算|
|`INT32`|`-`|`INT32`|`INT32`|減算|
|`INT32`|`*`|`INT32`|`INT32`|乗算|
|`INT32`|`×`|`INT32`|`INT32`|乗算|
|`INT32`|`/`|`INT32`|`Double`|除算|
