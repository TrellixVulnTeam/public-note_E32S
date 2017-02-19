#����ı���
	��������������������ͼ����
##�½�
* chapter1: Ruby����
	* [ruby�����﷨](https://github.com/lsj9383/Computation-Ruby/blob/master/cp1/ruby-basic.rb)
* chapter2: ����ĺ���
	* [С������](https://github.com/lsj9383/Computation-Ruby/blob/master/cp2/small-step.rb)
	* [������](https://github.com/lsj9383/Computation-Ruby/blob/master/cp2/big-step.rb)
* chapter3: ��򵥵ļ����
	* [ȷ�������Զ���](https://github.com/lsj9383/Computation-Ruby/blob/master/cp3/dfa.rb)
	* [��ȷ�������Զ���](https://github.com/lsj9383/Computation-Ruby/blob/master/cp3/nfa.rb)
	* [�������ƶ��ķ�ȷ�������Զ���](https://github.com/lsj9383/Computation-Ruby/blob/master/cp3/fm_nfa.rb)
	* [������ʽ](https://github.com/lsj9383/Computation-Ruby/blob/master/cp3/regular.rb)
	* [NFAתDFA](https://github.com/lsj9383/Computation-Ruby/blob/master/cp3/nfa2dfa.rb)
* chapter4: ��ǿ��������
	* [ȷ�������ƻ�](https://github.com/lsj9383/Computation-Ruby/blob/master/cp4/dpda.rb)
	* [��ȷ�������ƻ�](https://github.com/lsj9383/Computation-Ruby/blob/master/cp4/npda.rb)
	* [�﷨����]()
* chapter5: �ռ�����
	* [ͼ���](https://github.com/lsj9383/Computation-Ruby/blob/master/cp5/DTM.rb)
* chapter6: ���㿪ʼ���
	* [rubyģ��lambda����](https://github.com/lsj9383/Computation-Ruby/blob/master/cp6/ruby2lambda.rb)
	* [FizzBuzz](https://github.com/lsj9383/Computation-Ruby/blob/master/cp6/FizzBuzz.rb)
	* [lambda�߼����-Stream](https://github.com/lsj9383/Computation-Ruby/blob/master/cp6/lambda_advanced.rb)
	* [��](https://github.com/lsj9383/Computation-Ruby/blob/master/cp6/lambda.rb)
	
###ruby����
���ϴ����ʹ��ruby��װ����ʹ��rubyʱ�����漰��һЩ�����أ�����������Դ�ڹ��⣬�޷����أ����Ҫ������Դ��Ϊ���ھ����ڱ������У������漰����ʹ���﷨��������`treetop`�����Ҫ��������Դ<br>
<br>
* �鿴��ǰ����Դ:
```
gem sources
```
* ɾ����ǰ����Դ:
```
gem sources -r <url>
```
* ����µ�����Դ��Ŀǰ������ruby-chinaά��:
```
gem sources --add https://gems.ruby-china.org/
gem sources -l
```
* ʹ��gem��װtreetop:
```
gem install treetop
```
###Y�����

![](https://github.com/lsj9383/Computation-Ruby/blob/master/pic/Y.jpg)

����һ���ݹ麯���Ķ��壬����������뵽��
```
F = ->x{ ->y{ F[t[x]][t[y]] } }
```
��ʵ�ϣ�����**α�ݹ�**��һ����ƭ����Ϊ�ڶ���Fʱ������F��������ʱ��F��δ���壬�ںܶ���������ⲻ�ܽ��ͻ����(��ruby)��<br>
��Ϊ�������������ĵݹ��һ��ʵ�֣����Կ��ǽ������Լ���Ϊ���������Լ���������ʾ��
```
F = ->f { ->x { ->y { f[f][t[x]][t[y]] } } }
F[F][a][b]
```
��ȷʵ�������ĵݹ�, F��3�������Ĳ�������һ��������һ���߽׺������ú�������3����������һ�����������Խ�F�Լ������ȥ���������������ݹ�:
```
F[F][a][b] => F[F][t[a]][t[b]]
```
ע��, �����е�**P=>M**�����P�ĵ��ã�����õ���ĳһ����䡣<br>
���ǣ������������ʹ�÷��������ò����������������ó�ª��**F[F]**��**f[f]**��ʲô�����?<br>
Ϊ������ʽ����������ϣ���ܹ�������**f[f][][]**������ʽ������ֱ��**f[][]**����ʽ:
```
F = ->f{ ->x { ->y { f[x][y] }} }
```
����������ʽ�������ȥ�����أ�**F[a][b]**����**F[F][a][b]**��ʼ�ղ��ԡ�<br>
�������һ������**P**������**F[P][a][b]**��õ�**P[t[a]][t[b]]**������**F[P][a][b]**����**P[a][b]**�������á���ô���Եõ�����һ��������:
```
P[a][b] => F[P][a][b] => P[t[a]][t[b]]
```
Ҳ����˵��������һ������**P**,ʹ��**P => F[P]**��������ô**P[a][b]**�ͻ���һ�εݹ���á����ڿ�����ʽ����Y������ˡ�<br>
���ڴ�**P**��Ҫ�õ�**F[P]**����ô��ȻP�а�����**F**�������ּ������һ������**Y**��ʹ��**P = Y[F]**����ô�Ϳ��Եó�ֻҪ����:
**Y[F] = F[Y[F]]**
��ô��**Y[F][a][b]**�ĵ��ã����ǵݹ�ġ�������:
```
Y[F][a][b] => F[Y[F]][a][b] => Y[F][t[a]][t[b]] => ...
```
�������**Y[F] = F[Y[F]]**ʽ�ӣ��ͱ���ΪY����ӡ���Haskell�Ƶ�����Y����ӣ��Ѿ�������ͼƬ��չʾ�������������ʽ�ӡ�