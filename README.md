# Medical Questions — Data Management

Backend REST API de Quarkus para administrar preguntas de exámenes médicos almacenadas en PostgreSQL.

## Tecnología

- Java 17
- Quarkus 3.38.2
- Hibernate ORM con Panache
- PostgreSQL
- REST + Jackson
- Maven
- Docker

## API de preguntas médicas

Endpoint para obtener las preguntas:

```bash
curl --location 'http://localhost:8081/data-management/api/medical-questions'
```

La API devuelve los campos `id`, `questionNumber`, `examYear`, `questionText`, `alternatives`, `correctAnswer`, `explanation` y `status`.

## Ingresar a la BD

Para entrar a PostgreSQL dentro del contenedor Docker:

```bash
docker exec -it postgres-db psql -U user -d appdb
```

Una vez dentro de `psql`, se pueden ejecutar las sentencias SQL de este documento.

## Tabla `public.medical_questions`

La tabla almacena la pregunta, el año del examen, las alternativas en JSONB, la respuesta correcta, la explicación y el estado.

```sql
COMMENT ON TABLE public.medical_questions IS
    'Medical exam questions with alternatives, correct answer, and explanation.';

COMMENT ON COLUMN public.medical_questions.alternatives IS
    'JSONB object containing alternatives with keys A, B, C, D, and E.';
```

### Script de inserción de preguntas 57–68

Este script inserta las preguntas proporcionadas. Se conserva aquí para poder reutilizarlo. No elimina ni trunca la tabla. Si ya existen filas con la misma combinación de `question_number` y `exam_year` y la tabla tiene una restricción única para esas columnas, PostgreSQL puede rechazar las inserciones duplicadas.

```sql
INSERT INTO public.medical_questions
(question_number, exam_year, question_text, alternatives, correct_answer, explanation, status)
VALUES
(
  57, 'RM2017',
  'Mujer de 30 años, con un hijo vivo de 5 años, usuaria de DIU por 3 años que se lo retira por presentar cuadros de EPI a repetición, presencia de flujo vaginal y no presenta dismenorrea. No logra embarazarse desde hace dos años. ¿Cuál es la probable causa de la infertilidad?',
  '{"A":"Anovulación crónica","B":"Cervicitis crónica","C":"Endometriosis","D":"Obstrucción tubárica","E":"Adenomiosis"}'::jsonb,
  'D',
  'Es un caso de infertilidad secundaria: tuvo una gestación con anterioridad, pero no puede lograr una nueva gestación. La ausencia de dismenorrea aleja el diagnóstico de adenomiosis y endometriosis. No hay datos suficientes para plantear anovulación crónica. Una secuela importante de la EPI a repetición es la infertilidad por obstrucción tubárica.',
  'ACTIVE'
),
(
  58, 'RM2018',
  'Paciente de 28 años, acude por dos años de intento reproductivo sin éxito. Antecedentes de múltiples parejas sexuales. Examen: sin alteraciones. Histerosalpingografía: obstrucción de trompas. ¿Cuál es la causa más probable de infertilidad?',
  '{"A":"Hiperplasia endometrial","B":"Factor adherencial","C":"Malformaciones uterinas","D":"Miomatosis uterina","E":"Enfermedad pélvica inflamatoria"}'::jsonb,
  'E',
  'La infertilidad en parejas menores de 35 años se considera cuando intentan lograr un embarazo durante un año sin conseguirlo. Entre los factores femeninos se encuentran el factor cervical, uterino, ovárico y tuboperitoneal. La obstrucción de ambas trompas orienta a un factor tubárico-peritoneal; puede deberse a enfermedad inflamatoria pélvica por infecciones como clamidia, gonococo o tuberculosis, que dejan secuelas. Otras causas menos frecuentes incluyen cirugía tubárica previa y endometriosis severa.',
  'ACTIVE'
),
(
  59, 'RM2020',
  '¿Cuál es el factor asociado más frecuente de la infertilidad en el Perú?',
  '{"A":"Ovárico","B":"Cervical","C":"Uterino","D":"Tubárico","E":"Masculino"}'::jsonb,
  'D',
  'La infertilidad tiene factores dependientes del hombre y de la mujer. Respecto al factor femenino, el más frecuente es el factor tubárico, principalmente las adherencias y sinequias de las trompas uterinas. Entre sus causas están las infecciones, como la enfermedad pélvica inflamatoria. La explicación del documento queda cortada al final de la página.',
  'ACTIVE'
),
(
  60, 'RM2021',
  '¿Cuál es el factor de riesgo más importante asociado a infertilidad?',
  '{"A":"Tabaquismo","B":"Sobrepeso","C":"Infecciones","D":"Edad de la mujer","E":"Número de parejas"}'::jsonb,
  'C',
  'La infertilidad se define como la incapacidad de quedar embarazada a pesar de tener relaciones sexuales frecuentes y sin protección durante al menos un año. Las infecciones son una causa importante de infertilidad: las infecciones genitales en la mujer pueden producir enfermedad inflamatoria pélvica y daños permanentes en las trompas de Falopio, el útero y los tejidos circundantes, siendo responsables del factor tuboperitoneal, que representa aproximadamente el 30 % de las causas de infertilidad.',
  'ACTIVE'
),
(
  61, 'RM2015',
  'Adolescente de 16 años, que acude a consulta por presentar amenorrea primaria. Al examen físico: desarrollo de caracteres sexuales secundarios normales para su edad y presencia de tabique y vagina doble. ¿En qué otro aparato se espera encontrar malformación concomitante?',
  '{"A":"Urinario","B":"Óseo-muscular","C":"Cardiovascular","D":"Respiratorio","E":"Digestivo"}'::jsonb,
  'A',
  'Estamos ante un cuadro de amenorrea primaria con presencia de tabique vaginal. Cuando el tabique es longitudinal, se asocia con malformaciones urológicas; estas no se asocian del mismo modo si se trata de un tabique vaginal transversal.',
  'ACTIVE'
),
(
  62, 'RM2014',
  'Adolescente de 13 años, ingresa a emergencia por retención urinaria, con historia de amenorrea primaria y dolor cíclico en hipogastrio. Examen: masa abdominal palpable. ¿Cuál es el diagnóstico más probable?',
  '{"A":"Himen imperforado","B":"Duplicación de vagina","C":"Hipoplasia de vagina","D":"Quistes del conducto de Gartner","E":"Útero didelfo"}'::jsonb,
  'A',
  'El himen imperforado se presenta aproximadamente en 1 de cada 2.000 mujeres. Los síntomas que origina en la pubertad son amenorrea primaria, dolor abdominal o pélvico, retención de orina o estreñimiento; al examen suele encontrarse una masa abdominal. La alternativa se interpreta como “himen imperforado” por concordancia con el cuadro clínico y la explicación.',
  'ACTIVE'
),
(
  63, 'RM2016',
  'Paciente de 26 años, con amenorrea primaria, desarrollo mamario normal, con escaso vello púbico y axilar. Tiene informe de cariotipo XY. ¿Cuál es el diagnóstico?',
  '{"A":"Insensibilidad a los andrógenos","B":"Septum vaginal transverso completo","C":"Aplasia cervical","D":"Síndrome de Turner","E":"Síndrome de Mayer-Rokitansky"}'::jsonb,
  'A',
  'El desarrollo de caracteres sexuales secundarios está dado por el desarrollo mamario. El cariotipo XY descarta el septum vaginal transverso y la aplasia cervical como explicación principal. El síndrome de Turner corresponde a cariotipo XO y el síndrome de Mayer-Rokitansky se asocia con cariotipo XX y ausencia de útero. El cariotipo XY con fenotipo femenino y escaso vello púbico/axilar orienta a insensibilidad a los andrógenos.',
  'ACTIVE'
),
(
  64, 'RM2018',
  '¿Cuál es el tipo de amenorrea en una mujer con diagnóstico de anorexia nerviosa?',
  '{"A":"Hipotalámica","B":"Iatrogénica","C":"Ambiental","D":"Idiopática","E":"Autoinmunitaria"}'::jsonb,
  'A',
  'En la anorexia nerviosa puede encontrarse hipogonadismo hipogonadotrófico: disminuyen las señales hipofisarias, la secreción de LH y FSH y, por consecuencia, los niveles de estradiol plasmático. Esto corresponde a amenorrea de origen hipotalámico.',
  'ACTIVE'
),
(
  65, 'RM2022',
  'Mujer de 28 años, desde hace diez meses estuvo disminuyendo su flujo menstrual hasta encontrarse en amenorrea desde hace 4 meses. Examen: emaciada, útero de seis centímetros; anexos normales. Ecografía: útero de 6 cm; anexos normales. Se le indica estrógenos y progesterona por 10 días y luego de ello, la paciente no menstrúa. ¿Dónde se encuentra la falla?',
  '{"A":"Tiroides","B":"Útero","C":"Ovario","D":"Hipotálamo","E":"Hipófisis"}'::jsonb,
  'B',
  'Si una paciente no presenta respuesta (menstruación) luego de la sustitución hormonal, se puede descartar una falla en el eje hipotálamo-hipófiso-gonadal, por lo que se debe buscar otra causa, fundamentalmente uterina.',
  'ACTIVE'
),
(
  66, 'RM2024',
  'Mujer de 25 años, corredora de maratón, acude por ausencia de menstruación desde hace 9 meses. Examen: PA 100/60 mmHg; IMC 17. Laboratorio: FSH y LH disminuidos. Ecografía: útero de 7 cm, endometrio 4 mm. ¿Cuál es la causa más probable de la amenorrea?',
  '{"A":"Falla ovárica precoz","B":"Ejercicio físico","C":"Hipotiroidismo","D":"Síndrome de ovario poliquístico"}'::jsonb,
  'B',
  'La paciente presenta amenorrea secundaria asociada a historia de ejercicio físico intenso, IMC bajo y FSH/LH disminuidas, lo que sugiere hipogonadismo hipogonadotrópico. La causa más probable es la amenorrea hipotalámica funcional secundaria al ejercicio excesivo. El estrés metabólico y físico suprime el eje hipotálamo-hipofisario y altera la secreción pulsátil de GnRH. Puede asociarse con desórdenes menstruales, baja densidad ósea y déficit energético.',
  'ACTIVE'
),
(
  67, 'RM2017',
  '¿Cuál es el tratamiento inicial de la dismenorrea en las adolescentes?',
  '{"A":"Anticonceptivos orales","B":"Esteroides sistémicos","C":"Antiinflamatorios no esteroideos","D":"Oxitócicos","E":"Antieméticos"}'::jsonb,
  'C',
  'La dismenorrea es dolor asociado al ciclo menstrual. La dismenorrea primaria suele aparecer cerca de la menarquia y es funcional; la secundaria suele aparecer tiempo después y puede asociarse con patología estructural. En la dismenorrea primaria, el espasmo miometrial se relaciona con metabolitos del ácido araquidónico, por lo que los AINE son útiles y constituyen la primera elección.',
  'ACTIVE'
),
(
  68, 'RM2018',
  '¿Cuál es la terapia inicial en adolescente con dismenorrea?',
  '{"A":"Anticonceptivos","B":"Anticolinérgicos","C":"Corticoides","D":"AINEs","E":"Antihistamínicos"}'::jsonb,
  'D',
  'El dolor con la menstruación se denomina dismenorrea. Puede ser primaria cuando no hay patología orgánica, también llamada funcional, y suele coincidir con el inicio de la menarquia. Se debe al espasmo miometrial por la producción de metabolitos derivados del ácido araquidónico, por lo que la terapia de primera elección son los AINE. La dismenorrea secundaria aparece años después de la menarquia, con antecedente de periodos sin molestias, y suele deberse a causas estructurales como endometriosis, pólipos, miomas o EPI; el tratamiento es tratar la causa subyacente.',
  'ACTIVE'
);

-- Alinea el tipo SQL con el mapeo String de la entidad Java, si aún es CHAR(1).
ALTER TABLE medical_questions
ALTER COLUMN correct_answer TYPE VARCHAR(1)
USING TRIM(correct_answer)::VARCHAR(1);
```

## Configuración de Quarkus

La aplicación valida el esquema existente; no crea ni modifica tablas automáticamente:

```yaml
quarkus:
  hibernate-orm:
    schema-management:
      strategy: validate
```

Para la conexión local, la URL JDBC suele ser:

```text
jdbc:postgresql://localhost:5432/appdb
```

Si Quarkus corre dentro de Docker en la misma red que PostgreSQL, el hostname puede ser `postgres-db`, por ejemplo:

```text
jdbc:postgresql://postgres-db:5432/appdb
```

## Ejecutar en modo desarrollo

Windows PowerShell:

```powershell
mvnw.cmd quarkus:dev
```

Linux/macOS:

```bash
./mvnw quarkus:dev
```

La API se expone en:

```text
http://localhost:8081/data-management/api/medical-questions
```

## Verificar la tabla

Desde `psql`:

```sql
SELECT id, question_number, exam_year, correct_answer, status
FROM public.medical_questions
ORDER BY question_number, exam_year;
```

## Nota sobre duplicados

El script es un `INSERT` normal para conservar el comportamiento explícito de inserción. Si se ejecuta varias veces y existe una restricción única sobre `(question_number, exam_year)`, las filas duplicadas producirán un error. No se incluye `DROP TABLE`, `TRUNCATE` ni borrado automático de registros.
