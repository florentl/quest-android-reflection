# La Reflexion

La réflexion (reflection en anglais) permet l'introspection des classes, c'est-à-dire de charger une classe, d'en créer une instance et d'accéder aux membres statiques ou non (appel de méthodes, lire et écrire les attributs) sans connaître la classe par avance.

Java possède une API permettant la réflexion. Elle sert notamment à gérer des extensions (plug-in en anglais) pour une application.

## Objectifs

* Comprendre et manipuler la *Reflection*

## Etapes

### Mise en oeuvre

Le package *java.lang.reflect* est consacré à la réflexion. Comme vu plus haut, la structure d'un objet Java est caractérisée par une classe qui contient des constructeurs, des méthodes (ou fonctions) et enfin des champs. Les caractéristiques de l'objet sont représentés de manière formelle par une classe du package de réflexion : 

| Classe du package | Représente                |
|-------------------|---------------------------|
| Class             | Une classe.               |
| Method            | Une méthode d'une classe  |
| Constructor       | Constructeur d'une classe |
| Field             | Un champ d'une classe     |

#### Récupérer la classe d'un objet
Soit obj un objet quelconque, le code suivant donne sa *Class*. N'oublions pas que récupérer la classe est le point de départ pour avoir toutes les informations d'un objet 
```java
Class c = obj.getClass();
```

#### Récupérer une méthode
Du moment que l'on a récupéré la classe, on peut accéder aux méthodes de la classe. 
```java
// Récupère la classe
Class c = obj.getClass();
// Stocke toutes les méthodes dans un tableau
Method[] methods = c.getMethods();
// Récupère la méthode concat qui prend un String en paramètre
Method method = c.getMethod("concat", String.class);
```
Dans l'exemple ci-dessus, le premier paramètre de getMethod est le nom de la méthode recherchée. Attention le nom est sensible aux majuscules/minuscules. Les paramètres qui suivent sont les types des paramètres de la fonction. Ici, on veut la fonction concat avec exactement un paramètre de type String.

#### Récupérer un constructeur
Un constructeur, contrairement à une méthode, n'est pas identifié par son nom (puisque par définition, un constructeur porte toujours le nom de la classe) mais uniquement par le type de ses paramètres.
```java
// Récupère la classe
Class c = obj.getClass();
// Stocke tous les constructeurs dans un tableau
Constructor[] constructors = c.getConstructors();
// Récupère le constructeur qui prend un String en paramètre
Constructor constructor = c.getConstructor(String.class);
```

#### Récupérer un champ
Pour récupérer un champ, on utilise getField. 
```java
// Récupère la classe
Class c = obj.getClass();
// Stocke tous les champs dans un tableau
Field[] fields = c.getFields();
// Récupère le champs qui porte le nom "name"
Field field = c.getField("name");
```

#### Construire une instance
A partir de Constructor récupéré sur un type, il est très facile de construire une instance. On utilise pour cela la fonction newInstance. Cette fonction prend la liste des paramètres du constructeur.
```java
// Récupère la classe
Class c = obj.getClass();
// Récupère le constructeur qui prend un String en paramètre
Constructor constructor = c.getConstructor(String.class);
// Créé une instance de l'objet en appelant le constructeur prennant un String en paramètre.
Object instance = constructor.newInstance("");
```

#### Appel à une méthode
A partir de l'objet Method, on peut exécuter la méthode sur une instance de l'objet. On utilise la fonction invoke qui prend en paramètre une instance d'un objet sur laquelle exécuter la méthode et la liste des paramètres de la méthode. 
```java
// Récupère la classe
Class c = obj.getClass();
// Récupère le constructeur qui prend un String en paramètre
Constructor constructor = c.getConstructor(String.class);
// Créé une instance de l'objet en appelant le constructeur prennant un String en paramètre.
Object instance = constructor.newInstance("");
// Récupère la méthode concat qui prend un String en paramètre
Method method = c.getMethod("concat", String.class);
method.invoke(instance, " ");
```

#### Affectation d'une valeur à un champ
Pour affecter une valeur à un champ d'une instance d'un objet, on utilise la méthode set de Field du champ à modifier. 
```java
// Récupère la classe
Class c = obj.getClass();
// Récupère le constructeur qui prend un String en paramètre
Constructor constructor = c.getConstructor(String.class);
// Créé une instance de l'objet en appelant le constructeur prennant un String en paramètre.
Object instance = constructor.newInstance("");
// Modifie la valeur du champ name en lui assignant la chaine de caractère "toto"
Field field = c.getField("name");
field.set(instance, "toto");
```

#### Lecture de la valeur d'un champ
De la même manière, on peut lire la valeur d'un champ en utilisant la fonction get de Field. 
```java
// Lit la valeur du champ name
Field field = c.getField("name");
Object lu = field.get(instance);
```

#### Faire sauter la portée *private*
Etant donné que la *réflexion* permet au code d'exécuter des opérations illégales dans un code sans *réflexion*, telles que l'accès à des champs et méthodes privés, l'utilisation de la *réflexion* peut entraîner des effets secondaires inattendus, susceptibles de rendre le code dysfonctionnel et de détruire la portabilité. Le code utilisant la *réflexion* casse les abstractions et peut donc changer le comportement avec les mises à niveau de la plate-forme.

```java
// Récupère un champ à l'aide de la méthode getDeclaredField
Field field = pClass.getClass().getDeclaredField(key);
// Rend accessible le champ
field.setAccessible(true);
```
