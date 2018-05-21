# Description 

Ensemble de composants techniques communs pouvant être utilisés par tous les modules du projet.

# Usage


## Créer un lanceur spring batch

```java
	public static void main(String[] args) {
		System.exit(SpringBatchUtils.run(SpringBatchDemoApplication.class, args));
	}
```

## Logguer les jobs et les steps

```xml
	<job id="monJob" parent="abstractLogJob">
		<step id="loadPersonStep" parent="abstractLogStep">
			
		</step>
	</job>
```