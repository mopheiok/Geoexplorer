## 墨卡托投影坐标系

取零子午线或自定义原点经线($L0$)与赤道交点的投影为原点，零子午线或自定义原点经线的的投影为纵坐标$Y$轴，赤道的投影为横轴坐标$X$轴，构成墨卡托平面直角坐标系

## 墨卡托投影正反解公式

以下所列的转换公式都是基于椭球体

$a$	——	椭球体长半轴，单位米(m)

$b$	——	椭球体短半轴，单位米(m)

$f$	——	扁率：$f=\frac{a-b}{a}$

$e$	——	第一偏心率：$e=\frac{\sqrt{a^2-b^2}}{a}=\sqrt{1-\frac{b^2}{a^2}}$

${e}'$	——	第二偏心率：$e=\frac{\sqrt{a^2-b^2}}{b}=\sqrt{\frac{a^2}{b^2}-1}$

$N$	——	卯酉圈曲率半径，单位米(m)

$R$	——	子午圈曲率半径，单位米(m)

$B$	——	维度，			$L$	——	经度，单位弧度(rad)

$Y$	——	纵直角坐标，$X$	——	横直角坐标，单位米(m)

- 墨卡托投影正解公式

  $(L, B) \to (X, Y)$，标准维度$B0$，原点维度0，原点经度$L0$
  $$
  \begin{equation}
  \begin{aligned}
  X_E=&K(L-L0)\\
  Y_N=&K \ln [\tan (\frac{\pi}{4}+\frac{B}{2})*(\frac{1-e \sin B}{1+e \sin B})^{\frac{e}{2}}] \\
  K = &N_{B0}*\cos(B0)=\frac{a^2/b}{\sqrt{1+{e}'^2 *\cos^2(B0)}}*\cos(B0)
  \end{aligned}
  \end{equation}
  $$
  
- 墨卡托投影反解公式：

  $(X, Y) \to (L, B)$，标准维度$B0$，原点维度0，原点经度$L0$
  $$
  \begin{equation}
  \begin{aligned}
  L = & \frac{X_E}{K}+L0\\
  B=& \frac{\pi}{2}-2\arctan \{\exp{(-\frac{Y_N}{K})} * \exp{[\frac{e}{2}\ln(\frac{1-e\sin B}{1+e\sin B})]}\}
  \end{aligned}
  \end{equation}
  $$
  公式中$\exp$为自然对数底，维度B通过迭代计算很快就收敛了



## Web Mercator投影公式

- 正投影公式
  $$
  \begin{equation}
  \begin{aligned}
  X=&R*rlng\\
  Y=&R*\ln[\tan(\frac{\pi}{4}+\frac{rlat}{2})]
  \end{aligned}
  \end{equation}
  $$
  
- 反投影公式
  $$
  \begin{equation}
  \begin{aligned}
  rlng = &\frac{X}{R} \\
  rlat = & 2\arctan[\exp(\frac{Y}{R})] -\frac{\pi}{2}
  \end{aligned}
  \end{equation}
  $$
  
