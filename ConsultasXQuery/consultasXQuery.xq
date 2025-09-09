(:1.Nombre y país de todos los artistas.:)
for $artista in doc("artistas.xml")/artistas/artista
return <artista>
{$artista/nombreCompleto}
{$artista/pais}
</artista>
,
(:2.El nombre (sin etiquetas) de los artistas que nacieron antes de 1600.:)
for $artista in doc("artistas.xml")/artistas/artista[nacimiento < 1600]
return $artista/nombreCompleto/text()
,
(:Otra opcion:)
for $artista in doc("artistas.xml")/artistas/artista
where $artista/nacimiento < 1600
return $artista/nombreCompleto/text()
,
(:3.Nombre de los artistas para los que no hay año de fallecimiento.:)
for $artista in doc("artistas.xml")/artistas/artista
where not ($artista/fallecimiento)
return $artista/nombreCompleto
,
(:4.Una lista HTML con el nombre de los artistas nacidos en España.:)
<ul>{
for $artista in doc("artistas.xml")/artistas/artista
where $artista/pais = 'España'
return 
<li>
{$artista/nombreCompleto/text()}
</li>
}</ul>
,
(:Otra solucion del 4:)
<ul>{
for $artista in doc("artistas.xml")/artistas/artista[pais = 'España']
return 
<li>
{$artista/nombreCompleto/text()}
</li>
}</ul>
,
(:5.El número de artistas nacidos antes de 1500.:)
count(
for $artista in doc("artistas.xml")/artistas/artista[nacimiento < 1500]
return $artista/nombreCompleto/text()
)
,
(:Otra solucion del 5:)
count(
for $artista in doc("artistas.xml")/artistas/artista
where $artista/nacimiento < 1500
return $artista/nombreCompleto/text()
)
,
(:Otra solucion del 5 con let:)
let $artista:=
  for $artista in doc("artistas.xml")/artistas/artista
  where $artista/nacimiento < 1500
  return $artista/nombreCompleto/text()
return count ($artista)
,
(:6.Modelo de las impresoras de tipo “matricial”:)
for $impresora in doc("impresoras.xml")/impresoras/impresora
where $impresora/@tipo = 'matricial'
return $impresora/modelo
,
(:Otra forma:)
for $impresora in doc("impresoras.xml")/impresoras/impresora[@tipo = 'matricial']
return $impresora/modelo
,
(:7.Marca y modelo de las impresoras con un sólo tamaño .:)
for $impresora in doc("impresoras.xml")/impresoras/impresora
where count($impresora/tamaño) = 1
return <impresora>
<marca>{$impresora/marca/text()}</marca>
<modelo>{$impresora/modelo/text()}</modelo>
</impresora>
,
(:8.Marca y modelo de las impresoras con tamaño A3 pueden tener otros.:)
for $impresora in doc("impresoras.xml")/impresoras/impresora
where $impresora/tamaño = 'A3'
return <impresora>
<marca>{$impresora/marca/text()}</marca>
<modelo>{$impresora/modelo/text()}</modelo>
</impresora>
,
(:9.Marca y modelo de las impresoras con tamaño A4 como único tamaño .:)
for $impresora in doc("impresoras.xml")/impresoras/impresora
where $impresora/tamaño = 'A4' and count ($impresora/tamaño) = 1
return <impresora>
<marca>{$impresora/marca/text()}</marca>
<modelo>{$impresora/modelo/text()}</modelo>
</impresora>
,
(:10.Modelo de las impresoras en red.:)
for $impresora in doc("impresoras.xml")/impresoras/impresora
where exists ($impresora/enred)
return<modelo>{$impresora/modelo/text()}</modelo>