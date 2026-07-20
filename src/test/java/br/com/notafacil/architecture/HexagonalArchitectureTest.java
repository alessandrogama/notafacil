package br.com.notafacil.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "br.com.notafacil")
class HexagonalArchitectureTest {

    @ArchTest
    static final ArchRule hexagonalBoundaries = layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .withOptionalLayers(true)
            .layer("Domain").definedBy("br.com.notafacil.domain..")
            .layer("Application").definedBy("br.com.notafacil.application..")
            .layer("Infrastructure").definedBy("br.com.notafacil.infrastructure..")
            .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure");

    @ArchTest
    static final ArchRule domainIsFrameworkFree = noClasses()
            .that().resideInAPackage("br.com.notafacil.domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework..",
                    "jakarta.persistence..",
                    "com.fasterxml.jackson.."
            )
            .because("o núcleo de domínio deve estar puro, sem nenhuma dependência de framework (ADR-001)");
}
