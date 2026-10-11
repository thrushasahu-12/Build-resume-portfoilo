package com.example.model

enum class ProjectCategory(val displayName: String) {
    ALL("All Projects"),
    BI_DASHBOARDS("Power BI & Dashboards"),
    ETL_PIPELINES("ETL & Engineering"),
    STATISTICAL_ANALYSIS("A/B & Statistics"),
    AUTOMATION("Automation & Python")
}

enum class SimulationType {
    BEFORE_AFTER_EFFICIENCY,
    SQL_DIAGNOSTIC_QUERY,
    AB_TEST_SIGNIFICANCE,
    STAR_SCHEMA_WAREHOUSE,
    KPI_WHAT_IF
}

data class ProjectMetric(
    val label: String,
    val value: String,
    val delta: String,
    val subtext: String,
    val isPositive: Boolean = true
)

data class ArchitectureStage(
    val stepNumber: Int,
    val title: String,
    val tool: String,
    val shortDescription: String,
    val deepTechnicalDetails: String,
    val inputOutputSummary: String,
    val codeOrQuerySnippet: String = "",
    val snippetLanguage: String = "sql",
    val latencyMs: String = "450ms",
    val throughput: String = "25k rec/s",
    val faultToleranceStrategy: String = "Exponential backoff retry with dead-letter queue"
)

data class CodeSnippet(
    val title: String,
    val language: String,
    val code: String,
    val explanation: String
)

data class Project(
    val id: String,
    val title: String,
    val subtitle: String,
    val organization: String,
    val category: ProjectCategory,
    val period: String,
    val summary: String,
    val businessProblem: String,
    val businessImpact: String,
    val technicalArchitectureSummary: String,
    val architecturalTradeoffs: List<Pair<String, String>> = emptyList(),
    val challengesSolved: List<String> = emptyList(),
    val lessonsLearned: List<String> = emptyList(),
    val keyMetrics: List<ProjectMetric>,
    val architectureStages: List<ArchitectureStage>,
    val techStack: List<String>,
    val simulationType: SimulationType,
    val codeSnippets: List<CodeSnippet> = emptyList(),
    val liveDemoUrl: String = ""
)

data class ExperienceItem(
    val id: String,
    val role: String,
    val company: String,
    val subsidiary: String = "",
    val period: String,
    val duration: String,
    val location: String,
    val overview: String,
    val keyAchievements: List<String>,
    val metricsList: List<ProjectMetric>,
    val tools: List<String>
)

data class EducationItem(
    val degree: String,
    val institution: String,
    val period: String,
    val scoreOrDetails: String,
    val focusAreas: List<String>
)

data class CertificationItem(
    val id: String,
    val title: String,
    val issuer: String,
    val year: String,
    val credentialId: String,
    val verificationUrl: String,
    val skillsValidated: List<String>
)

data class SkillItem(
    val name: String,
    val proficiency: Int, // 0 to 100
    val experienceYears: String,
    val highlighted: Boolean = false
)

data class SkillGroup(
    val category: String,
    val iconName: String,
    val skills: List<SkillItem>
)

enum class BackgroundAnimationMode(val title: String, val subtitle: String) {
    CONSTELLATION("Data Constellation", "Dynamic floating nodes with pulsing network paths"),
    WAVE_FIELD("Analytics Wave Field", "Multi-frequency sine telemetry waves"),
    MATRIX_GRID("Quantum Metric Grid", "Isometric digital coordinate matrix with data pulses")
}

data class BackgroundSettings(
    val mode: BackgroundAnimationMode = BackgroundAnimationMode.CONSTELLATION,
    val speedMultiplier: Float = 1.0f,
    val glowIntensity: Float = 0.85f,
    val touchInteractive: Boolean = true
)
