# Q0 Calculator

<img src="https://github.com/skytomo221/Q0-Calculator/blob/master/images/icon.png?raw=true" width="200px">

**Q0 Calculator** は Java の課題のために作られた電卓プログラムです。  
そのため、単位が確定するまでは、このソースコードの利用をお控えください。

## 計算例

```jl
Input  => 1 + 1
Output => 2

Input  => 1 + 1 + 1
Output => 3

Input  => 1 + 2 * 3
Output => 7

Input  => (1 + 2) * 3
Output => 9

Input  => 1 ÷ 3
Output => 0.3333333333333333

Input  => 0.3333333333333333 × 3
Output => 1.0

Input  => -2 ^ 10
Output => -1024

Input  => 0xff + 0o77 + 0b11
Output => 321

Input  => 1¬¬ + ¬
Output => 88

Input  => "aa" < "ab" < "ac"
Output => true

Input  => 5 > 4 > 33 > 2 > 1
Output => false

Input  => 'a' + 1
Output => 'b'

Input  => "abc" * "def"
Output => "abcdef"

Input  => "abc" ^ 5
Output => "abcabcabcabcabc"

Input  => i = 1
          sum = 0
          while 1 <= i <= 10
            sum = sum + i
            i = i + 1
            sum
          end
Output => 55
```

## 型

|   型名    |         説明         |
| :-------: | :------------------: |
|  `Bool`   |        真理値        |
|  `Char`   |        真理値        |
|  `Int8`   |    8ビット整数型     |
|  `Int16`  |    16ビット整数型    |
|  `Int32`  |    32ビット整数型    |
|  `Int64`  |    64ビット整数型    |
| `Float32` | 単精度浮動小数点数型 |
| `Float64` | 倍精度浮動小数点数型 |
| `String`  |       文字列型       |

## リテラル

| リテラル |       表記        |
| :------: | :---------------: |
|  `0xff`  |    16進数表記     |
|  `0o77`  |     8進数表記     |
|  `0o11`  |     2進数表記     |
| `1.0e10` | Float64型指数表記 |
| `1.0f10` | Float32型指数表記 |
|   `¬`    |      フ記法       |

## 演算子の優先順位

| 優先順位 |                 演算子                 |
| :------: | :------------------------------------: |
|    9     |                  `()`                  |
|    8     |                  `^`                   |
|    7     |            `*` `/` `&` `%`             |
|    6     |      `+` `-` <code>&#124;</code>       |
|    5     |      `==` `!=` `<=` `<` `>` `>=`       |
|    4     |                  `&&`                  |
|    3     |       <code>&#124;&#124;</code>        |
|    2     |                  `=`                   |
|    1     | `begin ... end` `if` `if else` `while` |

## 演算子

### 算術演算子

|   式    |      名称      |                                                 説明                                                 |
| :-----: | :------------: | :--------------------------------------------------------------------------------------------------: |
|  `+x`   | 単項加算演算子 |                                符号を反転しないことを明示的に表します                                |
|  `-x`   | 単項減算演算子 |                                           符号を反転します                                           |
| `x + y` |   加算演算子   |                                加算演算子は2つの数値の和を返します。                                 |
| `x - y` |   減算演算子   |                   減算演算子は 1 つの数値から 1 つの数値を差し引き、差を返します。                   |
| `x * y` |   乗算演算子   |                            乗算演算子は数値を掛けあわせた結果を返します。                            |
| `x / y` |   除算演算子   |               除算演算子は左のオペランドを右のオペランドで割り引くことで商を返します。               |
| `x ^ y` |  べき乗演算子  |                    べき乗は 1 つ目の数値を 2 つ目の数値でべき乗した値を返します。                    |
| `x % y` |   剰余演算子   | 剰余演算子は 1 つ目の数値を 2 つ目の数値で割った余りを返します。剰余は常に被除数の符号を採用します。 |

### ビット演算子

|           式            |   名称   |                                           説明                                           |
| :---------------------: | :------: | :--------------------------------------------------------------------------------------: |
|         `x & y`         | ビット積 |        オペランドの対応するビットがともに 1 である各ビットについて 1 を返します。        |
| <code>x &#124; y</code> | ビット和 | オペランドの対応するビットがどちらかまたはともに 1 である各ビットについて 1 を返します。 |

### 比較演算子

|    式    |         名称         |                                     説明                                     |
| :------: | :------------------: | :--------------------------------------------------------------------------: |
| `x == y` |      等価演算子      |          等価演算子は、オペランド同士が等しいならば、真を返します。          |
| `x != y` |     不等価演算子     |       不等価演算子は、オペランド同士が等しくないならば、真を返します。       |
| `x <= y` | 小なりイコール演算子 | 小なりイコール演算子は、左オペランドが右オペランド以下ならば、真を返します。 |
| `x < y`  |     小なり演算子     |   小なり演算子は、左オペランドが右オペランドより小さければ、真を返します。   |
| `x >= y` | 大なりイコール演算子 | 大なりイコール演算子は、左オペランドが右オペランド以上ならば、真を返します。 |
| `x > y`  |     大なり演算子     |   大なり演算子は、左オペランドが右オペランドより大きければ、真を返します。   |

### 論理演算子

|    式    |    名称    |                               説明                                |
| :------: | :--------: | :---------------------------------------------------------------: |
| `x && y` | AND 演算子 | AND 演算子は、左右のオペランドがどちらも真ならば、真を返します。  |
| `x || y` | OR 演算子  | OR 演算子は、左右のオペランドがどちらかが真ならば、真を返します。 |

### 制御構文演算子

#### begin-end 演算子

複文演算子ともいいます。
この演算子は、ブロックの中に含まれる複数の式を順次実行します。
最後の式の値が戻り値になります。

```jl
Input  => z = begin
            x = 1
            y = 2
            x + y
          end
Output => 3
```

#### if 演算子

```jl
if "条件式"
  "ブロック"
end
```

この演算子は、条件式が `true` のとき、ブロックを実行します。
ブロックの最後の式の値が戻り値になります。
条件式が `false` のときは `false` が戻り値になります。

#### if-else 演算子

```jl
if "条件式"
  "if-ブロック"
else
  "else-ブロック"
end
```

この演算子は、条件式が `true` のとき、if-ブロックを実行し、
`false` のときは else-ブロックが実行されます。
実行されたブロックの最後の式の値が戻り値になります。

#### if-ifelse-else 句

```jl
if "条件式"
  "if-ブロック"
elseif "条件式"
  "elseif-ブロック"
else
  "else-ブロック"
end
```

この型は `if` `if-else` 演算子を組み合わせた糖衣構文です。
Q0 では上の文は下に置き換えられてから計算されます。

```jl
if "条件式"
  "if-ブロック"
else
  if "条件式"
    "if-ブロック"
  else
    "else-ブロック"
  end
end
```

`if` 演算子と `if-else` 演算子もまとめて `if-elseif-else` 句ということもあります。
`if-elseif-else` 句の使用例を下に示します。

```jl
Input  => x = 0.1 + 0.2
          y = 0.3
          if x < y
              "x is less than y"
          elseif x > y
              "x is greater than y"
          else
              "x is equal to y"
          end
Output => "x is greater than y"
```

## while 演算子

`while` 演算子は、条件式を評価し、その式が `true` である間、 `while` 演算子の評価を続けます。
条件式の評価が `false` に初めてなると、その後、本体は決して評価されません。
返り値は最後に評価された式の値が戻り値になります。

```jl
Input  => i = 1
          sum = 0
          while 1 <= i <= 10
            sum = sum + i
            i = i + 1
            sum
          end
Output => 55
```

上に示した計算式は `while` 演算子を使って、 1 から 10 までの和を求める式です。
これらの三つの計算式は以下のように計算されます。

```jl
$1 = (i = 1)
   = 1
$2 = (sum = 0)
   = 0
$3 = (while ((1 <= i) <= 10); ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while ((1 <= 1) <= 10); ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while (1 <= 10); ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (0 + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (0 + 1)); (i = (i + 1)); sum) end)
   = (while true; ((sum = 1); (i = (i + 1)); sum) end)
   = (while true; (1; (i = (i + 1)); sum) end)
   = (while true; (1; (i = (1 + 1)); sum) end)
   = (while true; (1; (i = 2); sum) end)
   = (while true; (1; 2; sum) end)
   = (while true; (12; 1) end)
   = (while true; 1 end)
   = (while ((1 <= i) <= 10); 1 end)
   = (while ((1 <= i) <= 10); 1 end)
   = (while ((1 <= i) <= 10); 1 end)
   = (while true; 1 end)
   = (while true; ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (1 + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (1 + 2)); (i = (i + 1)); sum) end)
   = (while true; ((sum = 3); (i = (i + 1)); sum) end)
   = (while true; (3; (i = (i + 1)); sum) end)
   = (while true; (3; (i = (2 + 1)); sum) end)
   = (while true; (3; (i = 3); sum) end)
   = (while true; (3; 3; sum) end)
   = (while true; (33; 3) end)
   = (while true; 3 end)
   = (while ((1 <= i) <= 10); 3 end)
   = (while ((1 <= i) <= 10); 3 end)
   = (while ((1 <= i) <= 10); 3 end)
   = (while true; 3 end)
   = (while true; ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (3 + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (3 + 3)); (i = (i + 1)); sum) end)
   = (while true; ((sum = 6); (i = (i + 1)); sum) end)
   = (while true; (6; (i = (i + 1)); sum) end)
   = (while true; (6; (i = (3 + 1)); sum) end)
   = (while true; (6; (i = 4); sum) end)
   = (while true; (6; 4; sum) end)
   = (while true; (64; 6) end)
   = (while true; 6 end)
   = (while ((1 <= i) <= 10); 6 end)
   = (while ((1 <= i) <= 10); 6 end)
   = (while ((1 <= i) <= 10); 6 end)
   = (while true; 6 end)
   = (while true; ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (6 + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (6 + 4)); (i = (i + 1)); sum) end)
   = (while true; ((sum = 10); (i = (i + 1)); sum) end)
   = (while true; (10; (i = (i + 1)); sum) end)
   = (while true; (10; (i = (4 + 1)); sum) end)
   = (while true; (10; (i = 5); sum) end)
   = (while true; (10; 5; sum) end)
   = (while true; (105; 10) end)
   = (while true; 10 end)
   = (while ((1 <= i) <= 10); 10 end)
   = (while ((1 <= i) <= 10); 10 end)
   = (while ((1 <= i) <= 10); 10 end)
   = (while true; 10 end)
   = (while true; ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (10 + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (10 + 5)); (i = (i + 1)); sum) end)
   = (while true; ((sum = 15); (i = (i + 1)); sum) end)
   = (while true; (15; (i = (i + 1)); sum) end)
   = (while true; (15; (i = (5 + 1)); sum) end)
   = (while true; (15; (i = 6); sum) end)
   = (while true; (15; 6; sum) end)
   = (while true; (156; 15) end)
   = (while true; 15 end)
   = (while ((1 <= i) <= 10); 15 end)
   = (while ((1 <= i) <= 10); 15 end)
   = (while ((1 <= i) <= 10); 15 end)
   = (while true; 15 end)
   = (while true; ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (15 + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (15 + 6)); (i = (i + 1)); sum) end)
   = (while true; ((sum = 21); (i = (i + 1)); sum) end)
   = (while true; (21; (i = (i + 1)); sum) end)
   = (while true; (21; (i = (6 + 1)); sum) end)
   = (while true; (21; (i = 7); sum) end)
   = (while true; (21; 7; sum) end)
   = (while true; (217; 21) end)
   = (while true; 21 end)
   = (while ((1 <= i) <= 10); 21 end)
   = (while ((1 <= i) <= 10); 21 end)
   = (while ((1 <= i) <= 10); 21 end)
   = (while true; 21 end)
   = (while true; ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (21 + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (21 + 7)); (i = (i + 1)); sum) end)
   = (while true; ((sum = 28); (i = (i + 1)); sum) end)
   = (while true; (28; (i = (i + 1)); sum) end)
   = (while true; (28; (i = (7 + 1)); sum) end)
   = (while true; (28; (i = 8); sum) end)
   = (while true; (28; 8; sum) end)
   = (while true; (288; 28) end)
   = (while true; 28 end)
   = (while ((1 <= i) <= 10); 28 end)
   = (while ((1 <= i) <= 10); 28 end)
   = (while ((1 <= i) <= 10); 28 end)
   = (while true; 28 end)
   = (while true; ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (28 + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (28 + 8)); (i = (i + 1)); sum) end)
   = (while true; ((sum = 36); (i = (i + 1)); sum) end)
   = (while true; (36; (i = (i + 1)); sum) end)
   = (while true; (36; (i = (8 + 1)); sum) end)
   = (while true; (36; (i = 9); sum) end)
   = (while true; (36; 9; sum) end)
   = (while true; (369; 36) end)
   = (while true; 36 end)
   = (while ((1 <= i) <= 10); 36 end)
   = (while ((1 <= i) <= 10); 36 end)
   = (while ((1 <= i) <= 10); 36 end)
   = (while true; 36 end)
   = (while true; ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (36 + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (36 + 9)); (i = (i + 1)); sum) end)
   = (while true; ((sum = 45); (i = (i + 1)); sum) end)
   = (while true; (45; (i = (i + 1)); sum) end)
   = (while true; (45; (i = (9 + 1)); sum) end)
   = (while true; (45; (i = 10); sum) end)
   = (while true; (45; 10; sum) end)
   = (while true; (4510; 45) end)
   = (while true; 45 end)
   = (while ((1 <= i) <= 10); 45 end)
   = (while ((1 <= i) <= 10); 45 end)
   = (while ((1 <= i) <= 10); 45 end)
   = (while true; 45 end)
   = (while true; ((sum = (sum + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (45 + i)); (i = (i + 1)); sum) end)
   = (while true; ((sum = (45 + 10)); (i = (i + 1)); sum) end)
   = (while true; ((sum = 55); (i = (i + 1)); sum) end)
   = (while true; (55; (i = (i + 1)); sum) end)
   = (while true; (55; (i = (10 + 1)); sum) end)
   = (while true; (55; (i = 11); sum) end)
   = (while true; (55; 11; sum) end)
   = (while true; (5511; 55) end)
   = (while true; 55 end)
   = (while ((1 <= i) <= 10); 55 end)
   = (while ((1 <= i) <= 10); 55 end)
   = (while ((1 <= i) <= 10); 55 end)
   = (while false; 55 end)
   = 55
```
