package com.example.data

import com.example.model.*

object PortfolioRepository {

    val candidateProfile = mapOf(
        "name" to "Thrusha Sahu",
        "title" to "Data Analyst & Business Intelligence Specialist",
        "tagline" to "Transforming Complex Big Data into Actionable Executive Insights & Automated Intelligence",
        "experienceYears" to "3+ Years",
        "currentStatus" to "Available for Full-time Roles & Contracts",
        "email" to "aneshsahu03@gmail.com",
        "github" to "https://github.com/thrushasahu-12",
        "portfolioUrl" to "https://thrushasahu-12.github.io/Resume-Website-portfolio/",
        "linkedin" to "https://www.linkedin.com/in/thrusha-sahu",
        "location" to "Hyderabad, India / Open to Remote & Relocation",
        "summary" to "Data Analyst with 3+ years of enterprise experience at ServiceNow (Magnit Global), specializing in end-to-end data pipelines, executive BI dashboard engineering, and automated reporting. Proficient in SQL, Python, Power BI, Tableau, Snowflake, and AWS. Proven track record of slashing reporting turnaround by 50%, improving data accuracy by 30%, and delivering 30+ mission-critical interactive dashboards for cross-functional stakeholders."
    )

    val coreStats = listOf(
        ProjectMetric(
            label = "Total Dashboards Built",
            value = "30+",
            delta = "Power BI & Tableau",
            subtext = "Executive & departmental KPI monitors"
        ),
        ProjectMetric(
            label = "Reporting Time Saved",
            value = "50%",
            delta = "Python & BI Automation",
            subtext = "Turned 40-hr manual cycles into automated refreshes"
        ),
        ProjectMetric(
            label = "Data Quality Boost",
            value = "+30%",
            delta = "ETL Validation & Audits",
            subtext = "Eliminated discrepancies & content tagging drift"
        ),
        ProjectMetric(
            label = "Enterprise Tenure",
            value = "3+ Yrs",
            delta = "ServiceNow Ecosystem",
            subtext = "July 2022 to October 2025"
        )
    )

    val projects = listOf(
        Project(
            id = "servicenow-kpi-automation",
            title = "ServiceNow Enterprise KPI Automation & Executive BI",
            subtitle = "End-to-end automated reporting pipeline reducing manual cycles by 50%",
            organization = "ServiceNow (Magnit Global)",
            category = ProjectCategory.AUTOMATION,
            period = "2023 - 2025",
            summary = "Engineered automated data ingestion and KPI tracking for content operations, sales, and executive management. Replaced fragmented spreadsheets with unified Power BI dashboards and Python scripts.",
            businessProblem = "Weekly operational reports required over 40 hours of manual spreadsheet collation across 5 disparate departmental teams. Formulas frequently broke, data definitions drifted across regions, and executives received critical SLA escalation updates days after incidents occurred.",
            businessImpact = "Reduced weekly reporting cycles from 40 hours to under 20 hours (-50% turnaround), while elevating data accuracy from 78% to 99.2%. 30+ executive dashboards now serve 120+ global leaders daily with automated 4x/day refreshes.",
            technicalArchitectureSummary = "Automated delta extraction from Snowflake using python-connector, followed by vectorized Pandas cleansing, business rule Derivations, and automated staging into high-performance Star-Schema tables with DAX time-intelligence in Power BI.",
            architecturalTradeoffs = listOf(
                "DirectQuery vs Tabular In-Memory Cache" to "Opted for scheduled Tabular cache refresh (4x/day) instead of DirectQuery to achieve sub-second dashboard render speeds across complex DAX aggregations without overloading Snowflake compute credits.",
                "Python Scripting vs Native Power Query" to "Utilized Python for heavy regex category cleansing and IQR outlier filtering before data reaches Power BI, reducing Power BI gateway transformation overhead by 68%."
            ),
            challengesSolved = listOf(
                "Inconsistent content category tagging across legacy versions reconciled via regex fuzzy mapping.",
                "Cross-regional timezone offset discrepancies resolved by standardizing all timestamps to UTC at the Bronze ingestion layer.",
                "SLA threshold breaches parameterized dynamically based on ticket priority matrix."
            ),
            lessonsLearned = listOf(
                "Decoupling transformation logic into Python staging files enabled reuse across both Power BI and ad-hoc Jupyter exploratory analyses.",
                "Row-Level Security (RLS) in Power BI is most maintainable when driven by an underlying Dim_Security mapping table."
            ),
            keyMetrics = listOf(
                ProjectMetric("Reporting Time", "-50%", "-20 hrs/wk", "Automated Python scripts"),
                ProjectMetric("Data Accuracy", "+30%", "99.2% Quality", "Automated discrepancy checks"),
                ProjectMetric("Dashboards", "30+", "Active Views", "Cross-departmental adoption")
            ),
            simulationType = SimulationType.BEFORE_AFTER_EFFICIENCY,
            techStack = listOf("Python", "Power BI", "Snowflake", "DAX", "SQL", "Pandas", "ETL"),
            architectureStages = listOf(
                ArchitectureStage(
                    stepNumber = 1,
                    title = "Data Ingestion & Extraction",
                    tool = "Snowflake & Python Connector",
                    shortDescription = "Scheduled extraction of raw operational records and unstructured ticket logs.",
                    deepTechnicalDetails = "Utilized snowflake-connector-python with cursor pooling to batch-extract delta records updated within the last 24 hours. Implemented retry logic with exponential backoff and MD5 checksum validation.",
                    inputOutputSummary = "Input: 250k+ raw incident rows/day -> Output: Clean staging parquet frames",
                    codeOrQuerySnippet = "# Batch extraction with Snowflake Connector\nimport snowflake.connector\nimport pandas as pd\n\nconn = snowflake.connector.connect(\n    user=CONFIG['USER'],\n    account=CONFIG['ACCOUNT'],\n    warehouse='ANALYTICS_WH',\n    database='SERVICENOW_DB'\n)\nquery = 'SELECT ticket_id, priority, created_at, resolved_at FROM incident_logs WHERE updated_at >= CURRENT_DATE - 1'\ndf_raw = pd.read_sql(query, conn)",
                    snippetLanguage = "python",
                    latencyMs = "620ms",
                    throughput = "42k rec/s",
                    faultToleranceStrategy = "3-tier retry with fallback to cached staging delta"
                ),
                ArchitectureStage(
                    stepNumber = 2,
                    title = "Automated Data Cleaning & Transformation",
                    tool = "Python (Pandas, NumPy)",
                    shortDescription = "Tagging normalization, null handling, and business metric derivation.",
                    deepTechnicalDetails = "Cleaned inconsistent status enumerations, calculated first-response latency and Resolution SLA minutes. Computed outlier bounds using Interquartile Range (IQR) filtering.",
                    inputOutputSummary = "Input: Staged raw data -> Output: Enriched dimensional dataset with SLA flags",
                    codeOrQuerySnippet = "# Deriving Business SLAs & Outlier Cleansing\ndf['resolution_hours'] = (df['resolved_at'] - df['created_at']).dt.total_seconds() / 3600\ndf['sla_breached'] = df['resolution_hours'] > 24\ndf['category_clean'] = df['raw_category'].str.lower().str.strip()",
                    snippetLanguage = "python",
                    latencyMs = "380ms",
                    throughput = "110k rec/s",
                    faultToleranceStrategy = "Quarantine corrupted records to dead-letter storage table"
                ),
                ArchitectureStage(
                    stepNumber = 3,
                    title = "Semantic Modeling & DAX Measures",
                    tool = "Power BI & DAX",
                    shortDescription = "Star-schema dimensional modeling with advanced time-intelligence measures.",
                    deepTechnicalDetails = "Constructed 1-to-many relationships connecting Fact_Incidents to Dim_Date, Dim_Department, and Dim_Priority. Authored dynamic DAX measures for MTTR and YoY variance.",
                    inputOutputSummary = "Input: Normalized star tables -> Output: High-speed Tabular In-Memory Model",
                    codeOrQuerySnippet = "// Mean Time To Resolution (MTTR)\nMTTR_Hours = \nVAR ResolvedTickets = FILTER(Fact_Incidents, Fact_Incidents[status] = \"Resolved\")\nRETURN\nAVERAGEX(ResolvedTickets, Fact_Incidents[resolution_hours])",
                    snippetLanguage = "dax",
                    latencyMs = "210ms",
                    throughput = "In-memory Tabular",
                    faultToleranceStrategy = "Pre-calculated summary tables for heavy YoY comparisons"
                ),
                ArchitectureStage(
                    stepNumber = 4,
                    title = "Executive BI Visualization & Alerting",
                    tool = "Power BI Service & Gateway",
                    shortDescription = "Interactive drill-down dashboards with automated scheduled refresh and alert triggers.",
                    deepTechnicalDetails = "Configured scheduled cloud gateway refreshes 4x daily. Built row-level security (RLS) so managers only view their respective department metrics.",
                    inputOutputSummary = "Final: 30+ interactive dashboard views accessed by 120+ global stakeholders",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "sql",
                    latencyMs = "85ms render",
                    throughput = "120 concurrent viewers",
                    faultToleranceStrategy = "Automated email alerts if gateway refresh fails"
                )
            ),
            codeSnippets = listOf(
                CodeSnippet(
                    title = "ETL Discrepancy Reconciliation Query",
                    language = "SQL",
                    code = "WITH daily_metrics AS (\n    SELECT \n        DATE_TRUNC('day', created_at) AS metric_date,\n        department,\n        COUNT(*) AS total_tickets,\n        COUNT(CASE WHEN sla_status = 'BREACHED' THEN 1 END) AS breaches\n    FROM service_tickets\n    GROUP BY 1, 2\n)\nSELECT metric_date, department, total_tickets, breaches\nFROM daily_metrics\nORDER BY metric_date DESC;",
                    explanation = "Executes daily aggregation and calculates department-level breach rates with null-safe division."
                )
            )
        ),
        Project(
            id = "support-escalation-root-cause",
            title = "Customer Support Escalation & Root-Cause Engine",
            subtitle = "SQL & statistical diagnostic investigation identifying drivers of escalation spikes",
            organization = "ServiceNow Data Analytics",
            category = ProjectCategory.STATISTICAL_ANALYSIS,
            period = "2024",
            summary = "Investigated root causes behind recurring customer support bottlenecks and ticket escalations using deep SQL window functions and multi-dimensional Pareto analysis.",
            businessProblem = "Tier 2 and Tier 3 engineering teams experienced a 35% surge in re-assigned tickets, overwhelming specialized developers with basic issues and inflating average resolution time to 4.8 days.",
            businessImpact = "Pinpointed top 3 root-cause failure clusters accounting for 68% of all escalated tickets. Supported leadership in implementing targeted runbooks and training for Tier 1 agents, reducing escalations by 24% in Q2.",
            technicalArchitectureSummary = "Analytical SQL queries leveraging Window Functions, CTEs, and frequency clustering, feeding an interactive Tableau diagnostic visualizer.",
            architecturalTradeoffs = listOf(
                "Window Functions vs Self-Joins" to "Employed LAG() and LEAD() window functions partitioned by ticket_id rather than multi-table self-joins, reducing database memory usage by 74% and execution time from 45s to 2.4s.",
                "TF-IDF vs Manual Tagging" to "Applied TF-IDF keyword extraction on unstructured resolution text to automatically discover emerging defect clusters before manual tags were updated."
            ),
            challengesSolved = listOf(
                "Tickets undergoing multiple round-trip handoffs identified via LAG() partition lag calculation.",
                "Filtered out auto-generated spam and test tickets using regex patterns on customer email domains."
            ),
            lessonsLearned = listOf(
                "Root causes are rarely single points of failure; 68% of escalations occurred when API timeouts coincided with misconfigured OAuth scopes."
            ),
            keyMetrics = listOf(
                ProjectMetric("Escalation Reduction", "-24%", "Root-cause fixes", "Direct operational savings"),
                ProjectMetric("Pareto Factor", "68%", "Top 3 Categories", "Pinpointed primary drivers"),
                ProjectMetric("Query Runtime", "2.4s", "Optimized indices", "From 45s down to 2.4s")
            ),
            simulationType = SimulationType.SQL_DIAGNOSTIC_QUERY,
            techStack = listOf("SQL", "PostgreSQL", "Tableau", "Root Cause Analysis", "Python", "Data Storytelling"),
            architectureStages = listOf(
                ArchitectureStage(
                    stepNumber = 1,
                    title = "Telemetry Aggregation & Pre-Filtering",
                    tool = "SQL (PostgreSQL / Snowflake)",
                    shortDescription = "Extracted 180,000 incident events with multi-tier escalation timestamps.",
                    deepTechnicalDetails = "Constructed Common Table Expressions (CTEs) to isolate tickets that underwent re-assignment or escalation status flips.",
                    inputOutputSummary = "Input: 180,000 incident history logs -> Output: 14,200 escalated cohort items",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "sql",
                    latencyMs = "950ms",
                    throughput = "180k rows",
                    faultToleranceStrategy = "Partition pruning by fiscal quarter"
                ),
                ArchitectureStage(
                    stepNumber = 2,
                    title = "Cohort Window Functions & Re-Assignment Tracking",
                    tool = "Advanced SQL Window Functions",
                    shortDescription = "Measured re-assignment hops, lag intervals, and handoff friction.",
                    deepTechnicalDetails = "Utilized LAG() and LEAD() window functions partitioned by ticket_id to calculate delay between support tier handoffs.",
                    inputOutputSummary = "Input: Escalation cohort -> Output: Handoff velocity and bottleneck matrix",
                    codeOrQuerySnippet = "SELECT \n    ticket_id, support_tier, assigned_at,\n    LAG(assigned_at) OVER(PARTITION BY ticket_id ORDER BY assigned_at) AS prev_assigned_at,\n    COUNT(*) OVER(PARTITION BY ticket_id) AS total_hops\nFROM ticket_assignment_history;",
                    snippetLanguage = "sql",
                    latencyMs = "420ms",
                    throughput = "14.2k items",
                    faultToleranceStrategy = "Null-safe coalesce on first assignment"
                ),
                ArchitectureStage(
                    stepNumber = 3,
                    title = "Root Cause Categorization & Clustering",
                    tool = "Python NLP & Scikit-Learn",
                    shortDescription = "TF-IDF topic modeling and frequency clustering on ticket resolution summaries.",
                    deepTechnicalDetails = "Grouped ticket resolution notes into distinct defect taxonomy: API Timeout, Token Refresh Misconfiguration, Permission Missing, and Legacy UI Bug.",
                    inputOutputSummary = "Classified unstructured resolution notes into 6 distinct root-cause buckets.",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "python",
                    latencyMs = "1.1s",
                    throughput = "K-Means k=6",
                    faultToleranceStrategy = "Stopword sanitization and lemmatization"
                ),
                ArchitectureStage(
                    stepNumber = 4,
                    title = "Executive Diagnostic Dashboard",
                    tool = "Tableau / Power BI",
                    shortDescription = "Sankey diagram of ticket paths and Pareto distribution of issue categories.",
                    deepTechnicalDetails = "Built interactive filters allowing tier managers to inspect exact bottleneck stages and SLA risks.",
                    inputOutputSummary = "De-escalation strategy adopted across 4 regional support hubs.",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "sql",
                    latencyMs = "120ms",
                    throughput = "Interactive",
                    faultToleranceStrategy = "Daily pre-aggregated summary tables"
                )
            ),
            codeSnippets = listOf()
        ),
        Project(
            id = "ab-testing-content-optimization",
            title = "A/B Testing & Content Strategy Optimization",
            subtitle = "Statistical experimentation framework driving user engagement and conversion uplift",
            organization = "Marketing & Content Analytics",
            category = ProjectCategory.STATISTICAL_ANALYSIS,
            period = "2023 - 2024",
            summary = "Designed and analyzed A/B split tests on user onboarding funnels, landing layouts, and educational documentation to maximize user retention and completion rates.",
            businessProblem = "Product adoption for new module documentation had plateaued at 8.4%. Product and marketing disagreed on whether long-form interactive tutorials or quick modular check-lists drove stronger long-term retention.",
            businessImpact = "Statistically proved that modular check-lists (Variant B) delivered a +14.2% relative conversion lift at 98.7% statistical confidence (p = 0.008), leading to 100% rollout and higher 30-day active retention.",
            technicalArchitectureSummary = "Statistical hypothesis testing (Two-tailed Z-test, Chi-square, and Welch's t-test) in Python (SciPy & Statsmodels) with automated cohort monitoring.",
            architecturalTradeoffs = listOf(
                "Frequentist Z-Test vs Bayesian A/B" to "Selected Frequentist Z-Test with fixed-horizon power calculation for straightforward communication to cross-functional marketing stakeholders.",
                "SRM Validation Check" to "Enforced automated Chi-Square goodness-of-fit check before analyzing metrics to prevent misleading decisions caused by sample allocation bias."
            ),
            challengesSolved = listOf(
                "Sample Ratio Mismatch (SRM) detected and corrected during early test phase.",
                "Accounted for weekend vs weekday seasonality by running the experiment across two complete 7-day cycles."
            ),
            lessonsLearned = listOf(
                "Statistical significance (p < 0.05) must always be paired with Practical Significance (Minimum Detectable Effect) to avoid premature rollouts."
            ),
            keyMetrics = listOf(
                ProjectMetric("Conversion Uplift", "+14.2%", "Variant B Challenger", "Statistically validated"),
                ProjectMetric("Confidence Level", "98.7%", "p = 0.008", "Statistically significant"),
                ProjectMetric("Sample Size", "48,500", "Unique Users", "Evenly distributed split")
            ),
            simulationType = SimulationType.AB_TEST_SIGNIFICANCE,
            techStack = listOf("Python", "SciPy", "Statsmodels", "A/B Testing", "Google Analytics", "Pandas"),
            architectureStages = listOf(
                ArchitectureStage(
                    stepNumber = 1,
                    title = "Experiment Design & Power Calculation",
                    tool = "Python Statsmodels",
                    shortDescription = "Determined Minimum Detectable Effect (MDE) and required sample size.",
                    deepTechnicalDetails = "Calculated sample size needed for 80% statistical power at alpha = 0.05, establishing a 14-day observation window.",
                    inputOutputSummary = "Target: N=24,000 per variation to detect >= 5% relative uplift.",
                    codeOrQuerySnippet = "from statsmodels.stats.power import NormalIndPower\neffect_size = 0.05\npower_analysis = NormalIndPower()\nsample_size = power_analysis.solve_power(effect_size=effect_size, alpha=0.05, power=0.8)",
                    snippetLanguage = "python",
                    latencyMs = "50ms",
                    throughput = "N=48.5k",
                    faultToleranceStrategy = "Strict 14-day duration to avoid peeking bias"
                ),
                ArchitectureStage(
                    stepNumber = 2,
                    title = "Data Validation & SRM Sanity Check",
                    tool = "Chi-Square Goodness-of-Fit",
                    shortDescription = "Verified 50/50 traffic split to rule out Sample Ratio Mismatch (SRM).",
                    deepTechnicalDetails = "Checked observed counts (24,310 vs 24,190) against expected 50% split with Chi-Square (p = 0.58), confirming no telemetry bias.",
                    inputOutputSummary = "Status: PASSED (No traffic allocation skew).",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "python",
                    latencyMs = "35ms",
                    throughput = "p=0.58",
                    faultToleranceStrategy = "Automated test halt if Chi-Square p < 0.01"
                ),
                ArchitectureStage(
                    stepNumber = 3,
                    title = "Hypothesis Testing & Effect Estimation",
                    tool = "Two-Proportion Z-Test & Confidence Intervals",
                    shortDescription = "Evaluated baseline (8.4%) versus challenger (9.6%) conversion rate.",
                    deepTechnicalDetails = "Calculated 95% Confidence Interval for difference in proportions [+0.4%, +2.0%]. Computed p-value = 0.008 < 0.01.",
                    inputOutputSummary = "Control: 8.4% | Variant B: 9.6% | Relative Lift: +14.28%",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "python",
                    latencyMs = "40ms",
                    throughput = "p=0.008",
                    faultToleranceStrategy = "Two-sided Z-test with pooled variance"
                ),
                ArchitectureStage(
                    stepNumber = 4,
                    title = "Cohort Retention & Long-Term Follow-up",
                    tool = "Google Analytics & Looker Studio",
                    shortDescription = "Evaluated Day-7 and Day-30 retention to confirm lasting quality.",
                    deepTechnicalDetails = "Ensured lift was not driven by short-term novelty bias. Variant B sustained superior Day-30 retention.",
                    inputOutputSummary = "Deployed Variant B to 100% of global production traffic.",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "python",
                    latencyMs = "150ms",
                    throughput = "Production rollout",
                    faultToleranceStrategy = "Phased 25% -> 50% -> 100% feature flag ramp"
                )
            ),
            codeSnippets = listOf()
        ),
        Project(
            id = "multi-source-cloud-etl",
            title = "Multi-Source Cloud Data Warehouse & ETL Pipeline",
            subtitle = "Scalable automated data ingestion unifying AWS S3, Snowflake, and flat files",
            organization = "Enterprise Analytics Architecture",
            category = ProjectCategory.ETL_PIPELINES,
            period = "2023",
            summary = "Architected a resilient automated ETL pipeline consolidating multi-source logs, CRM exports, and Snowflake tables into a clean Dimensional Star Schema for self-service BI.",
            businessProblem = "Marketing, Customer Operations, and Finance maintained conflicting customer account records across disparate databases and S3 buckets. Business intelligence queries took hours and generated conflicting revenue figures.",
            businessImpact = "Eliminated 15+ disconnected departmental spreadsheets and unified 4 major data sources into a Single Source of Truth in Snowflake. Data latency dropped from 24 hours down to under 30 minutes.",
            technicalArchitectureSummary = "Automated Python extraction scripts loading into Snowflake staging tables, transformed into Fact & Dimension schemas with automated anomaly detection.",
            architecturalTradeoffs = listOf(
                "Star Schema vs Flattened Wide Tables" to "Adopted Star Schema with surrogate keys to drastically decrease storage footprint and enable fast join indexing in Snowflake.",
                "MD5 Checksums vs Timestamp Polling" to "Used MD5 hash comparison on incoming S3 prefixes to prevent duplicate loads when cloud files were re-uploaded with identical contents."
            ),
            challengesSolved = listOf(
                "Handled Slowly Changing Dimensions (SCD Type 2) for client department changes.",
                "Automated dead-letter queue for schema mismatch alerts."
            ),
            lessonsLearned = listOf(
                "Clear data contracts between producers and analysts prevent 90% of downstream reporting anomalies."
            ),
            keyMetrics = listOf(
                ProjectMetric("Sources Unified", "4 Sources", "Snowflake, S3, CSV, REST", "Centralized warehouse"),
                ProjectMetric("Data Latency", "< 30 mins", "Automated cron", "From 24-hr delay"),
                ProjectMetric("Discrepancies", "-30%", "Validation rules", "Automated reconciliation")
            ),
            simulationType = SimulationType.STAR_SCHEMA_WAREHOUSE,
            techStack = listOf("AWS", "Snowflake", "Python", "SQL", "Data Modeling", "ETL", "Jira"),
            architectureStages = listOf(
                ArchitectureStage(
                    stepNumber = 1,
                    title = "Source Data Extraction",
                    tool = "Python & AWS S3 Boto3",
                    shortDescription = "Ingestion from AWS S3 buckets, REST endpoints, and Snowflake tables.",
                    deepTechnicalDetails = "Batch file watcher monitoring S3 prefix with md5 checksum verification to prevent redundant loads.",
                    inputOutputSummary = "15GB daily event logs + CRM exports staged cleanly.",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "python",
                    latencyMs = "1.4s",
                    throughput = "15 GB/day",
                    faultToleranceStrategy = "Incremental prefix cursor checkpointing"
                ),
                ArchitectureStage(
                    stepNumber = 2,
                    title = "Schema Validation & Null Sanitization",
                    tool = "Great Expectations / Custom Python Tests",
                    shortDescription = "Automated data contracts checking primary keys, foreign keys, and ranges.",
                    deepTechnicalDetails = "Flagged defective rows to a quarantine dead-letter table with automated alerts sent to slack/email.",
                    inputOutputSummary = "99.98% clean pass rate into warehouse bronze layer.",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "python",
                    latencyMs = "650ms",
                    throughput = "25k rec/s",
                    faultToleranceStrategy = "Dead-letter quarantine isolation"
                ),
                ArchitectureStage(
                    stepNumber = 3,
                    title = "Dimensional Star Schema Modeling",
                    tool = "Snowflake SQL (Silver & Gold Layers)",
                    shortDescription = "Transformed flat tables into Fact_Activity and conforming Dimension tables.",
                    deepTechnicalDetails = "Surrogate key generation with MD5 hashing, slowly changing dimension (SCD Type 1 & 2) handling for client profiles.",
                    inputOutputSummary = "Fact_Incident joined with Dim_Date, Dim_Department, Dim_Agent.",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "sql",
                    latencyMs = "820ms",
                    throughput = "Partition pruned",
                    faultToleranceStrategy = "Idempotent MERGE statement transactions"
                ),
                ArchitectureStage(
                    stepNumber = 4,
                    title = "Semantic Consumption Layer",
                    tool = "Power BI & Tableau Direct Connect",
                    shortDescription = "High-performance curated views with partitioned indexing.",
                    deepTechnicalDetails = "Enabled business users to drag-and-drop trusted metrics without writing complex SQL joins.",
                    inputOutputSummary = "Zero manual VLOOKUPs required across 5 operational teams.",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "sql",
                    latencyMs = "95ms",
                    throughput = "Self-service BI",
                    faultToleranceStrategy = "Automated semantic model health monitoring"
                )
            ),
            codeSnippets = listOf()
        ),
        Project(
            id = "global-sales-bi-forecasting",
            title = "Global Sales & Performance Intelligence Dashboard",
            subtitle = "Interactive Power BI executive suite with dynamic currency and KPI scenario modeling",
            organization = "Business Intelligence Portfolio",
            category = ProjectCategory.BI_DASHBOARDS,
            period = "2024",
            summary = "Designed an enterprise-grade commercial intelligence dashboard featuring multi-currency conversion, product category margin analytics, and what-if parameter modeling.",
            businessProblem = "Global leadership struggled to compare sales figures across 12 countries due to fluctuating currency exchange rates and disconnected pricing promotions.",
            businessImpact = "Equipped regional VPs with dynamic currency normalization and what-if margin modeling, enabling data-driven pricing reviews and improving forecast precision by 18%.",
            technicalArchitectureSummary = "Complex DAX calculations, disconnected parameter tables for scenario modeling, and mobile-optimized responsive layout.",
            architecturalTradeoffs = listOf(
                "Disconnected Parameter Tables vs Hardcoded Logic" to "Implemented disconnected What-If parameter tables in DAX, enabling real-time margin sensitivity simulations without requiring database roundtrips.",
                "Dynamic Currency Conversion Layer" to "Maintained a daily currency exchange dimension table linked dynamically through DAX TREATAS() functions."
            ),
            challengesSolved = listOf(
                "Handled multi-currency historical conversion using transactional date exchange rate lookups.",
                "Implemented Stephen Few's dashboard UX principles to prevent cognitive overload for senior executives."
            ),
            lessonsLearned = listOf(
                "Executive adoption rises dramatically when dashboards answer 'What happens if we change X?' rather than just 'What happened last month?'"
            ),
            keyMetrics = listOf(
                ProjectMetric("Data Volume", "1.2M Rows", "Real-time query", "DirectQuery & Tabular Cache"),
                ProjectMetric("Drill-Down Depth", "4 Levels", "Region > Country > Rep > SKU", "Dynamic hierarchy"),
                ProjectMetric("Scenario Models", "What-If", "Price & Lead time sliders", "Interactive simulation")
            ),
            simulationType = SimulationType.KPI_WHAT_IF,
            techStack = listOf("Power BI", "DAX", "SQL", "Microsoft Excel", "Financial Modeling", "Data Storytelling"),
            architectureStages = listOf(
                ArchitectureStage(
                    stepNumber = 1,
                    title = "Transactional Ingestion",
                    tool = "SQL Views",
                    shortDescription = "Consolidated global invoices and sales opportunity records.",
                    deepTechnicalDetails = "Constructed unified views abstracting multiple regional ERP databases into uniform column schema.",
                    inputOutputSummary = "Standardized 12 regional currencies into USD base.",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "sql",
                    latencyMs = "510ms",
                    throughput = "1.2M rows",
                    faultToleranceStrategy = "Failover secondary database read replica"
                ),
                ArchitectureStage(
                    stepNumber = 2,
                    title = "Advanced DAX & Parameter Modeling",
                    tool = "Power BI Tabular",
                    shortDescription = "Implemented what-if parameter tables for dynamic pricing scenarios.",
                    deepTechnicalDetails = "Authored DAX measures that evaluate margin impacts in real time when user toggles discount sensitivity sliders.",
                    inputOutputSummary = "Dynamic real-time margin calculation without reload.",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "dax",
                    latencyMs = "140ms",
                    throughput = "Interactive",
                    faultToleranceStrategy = "Pre-computed currency matrix"
                ),
                ArchitectureStage(
                    stepNumber = 3,
                    title = "UX & Visual Hierarchy Design",
                    tool = "Material 3 Visual Design & Custom Themes",
                    shortDescription = "Card-based layout with visual contrast, trend sparklines, and status badges.",
                    deepTechnicalDetails = "Adhered to Stephen Few's dashboard design principles: high data-to-ink ratio, no clutter, clear visual callouts.",
                    inputOutputSummary = "Praised by regional managers for simplicity and speed.",
                    codeOrQuerySnippet = "",
                    snippetLanguage = "sql",
                    latencyMs = "60ms",
                    throughput = "Instant",
                    faultToleranceStrategy = "Mobile responsive layout viewport"
                )
            ),
            codeSnippets = listOf()
        )
    )

    val experiences = listOf(
        ExperienceItem(
            id = "servicenow-magnit",
            role = "Data Analyst",
            company = "ServiceNow",
            subsidiary = "Payroll: Magnit Global Private Limited",
            period = "July 2022 - October 2025",
            duration = "3 Years 4 Months",
            location = "Hyderabad, India / Hybrid",
            overview = "Served as a core Data Analyst delivering analytics solutions, executive dashboards, and automated ETL pipelines across operations, marketing, and cross-functional teams.",
            keyAchievements = listOf(
                "Developed and deployed 30+ interactive enterprise dashboards using Power BI, Tableau, and Google Data Studio, elevating visibility for department heads.",
                "Automated recurring performance reports and operational KPI dashboards using Python and Power BI, slashing regular reporting turnaround by 50%.",
                "Designed and implemented ETL data pipelines connecting Snowflake, cloud repositories, and flat files, establishing resilient data flows.",
                "Diagnosed and resolved historical data discrepancies and content tagging inconsistencies, elevating data quality and reporting accuracy by 30%.",
                "Conducted ad-hoc exploratory investigations using advanced SQL queries to diagnose root causes behind customer support escalations and churn trends.",
                "Applied statistical A/B testing methods to evaluate user behavior, driving optimized content strategies and conversion enhancements.",
                "Collaborated actively with stakeholders in Agile/Scrum sprints, translating complex business questions into clear, actionable data stories."
            ),
            metricsList = listOf(
                ProjectMetric("Turnaround", "-50%", "Reporting Time", "Automated with Python"),
                ProjectMetric("Accuracy", "+30%", "Quality Metric", "Resolved tagging anomalies"),
                ProjectMetric("Dashboards", "30+", "Delivered", "Power BI & Tableau"),
                ProjectMetric("Ad-hoc Analyses", "100+", "Completed", "Fast-turnaround queries")
            ),
            tools = listOf("Power BI", "Python", "SQL", "Snowflake", "Tableau", "AWS", "Google Data Studio", "ServiceNow Tool", "Git", "Jira", "Confluence", "Excel")
        )
    )

    val education = listOf(
        EducationItem(
            degree = "Bachelor of Technology (B.Tech)",
            institution = "Holy Mary Institute of Technology",
            period = "2014 - 2019",
            scoreOrDetails = "Computer Science and Engineering",
            focusAreas = listOf(
                "Database Management Systems (DBMS)",
                "Data Structures & Algorithms",
                "Statistical Methods & Probability",
                "Software Engineering Principles"
            )
        )
    )

    val certifications = listOf(
        CertificationItem(
            id = "google-foundations-data",
            title = "Foundations: Data, Data, Everywhere",
            issuer = "Google / Coursera",
            year = "2024",
            credentialId = "GOOG-DATA-FOUND-2024",
            verificationUrl = "https://coursera.org/verify",
            skillsValidated = listOf("Data Ecosystem", "Analytical Thinking", "SQL Foundations", "Data Ethics")
        ),
        CertificationItem(
            id = "edx-columbia-data-science",
            title = "Enabling Technologies for Data Science and Analytics",
            issuer = "edX / Columbia University",
            year = "2020",
            credentialId = "EDX-COLUMBIA-DATA-SCI",
            verificationUrl = "https://courses.edx.org",
            skillsValidated = listOf("Big Data Architecture", "Cloud Computing", "Data Pipelines", "Statistical Modeling")
        ),
        CertificationItem(
            id = "ms-excel-functions",
            title = "Functions in Microsoft Excel",
            issuer = "Microsoft Certified Learning",
            year = "2024",
            credentialId = "MS-EXCEL-FN-2024",
            verificationUrl = "https://learn.microsoft.com",
            skillsValidated = listOf("Advanced Formulas", "VLOOKUP/XLOOKUP", "Index-Match", "Dynamic Arrays")
        ),
        CertificationItem(
            id = "ms-excel-specialist",
            title = "Microsoft Certified Application Specialist: Excel",
            issuer = "Microsoft",
            year = "2017",
            credentialId = "MCAS-EXCEL-SPECIALIST",
            verificationUrl = "https://learn.microsoft.com",
            skillsValidated = listOf("Financial Modeling", "Pivot Tables & Slicers", "Macro Automation", "Data Cleansing")
        )
    )

    val skillGroups = listOf(
        SkillGroup(
            category = "Core Languages & Querying",
            iconName = "code",
            skills = listOf(
                SkillItem("SQL (PostgreSQL, Snowflake, MySQL)", 95, "3+ Years", highlighted = true),
                SkillItem("Python (Pandas, NumPy, Matplotlib)", 90, "3+ Years", highlighted = true),
                SkillItem("DAX (Data Analysis Expressions)", 88, "3 Years", highlighted = true),
                SkillItem("Power Query / M Language", 85, "3 Years")
            )
        ),
        SkillGroup(
            category = "BI & Data Visualization",
            iconName = "dashboard",
            skills = listOf(
                SkillItem("Power BI (Desktop & Service)", 95, "3+ Years", highlighted = true),
                SkillItem("Tableau", 88, "2+ Years", highlighted = true),
                SkillItem("Google Data Studio / Looker Studio", 85, "2+ Years"),
                SkillItem("Executive KPI Storytelling", 92, "3+ Years", highlighted = true)
            )
        ),
        SkillGroup(
            category = "Cloud & Data Warehousing",
            iconName = "cloud",
            skills = listOf(
                SkillItem("Snowflake Data Warehouse", 88, "2+ Years", highlighted = true),
                SkillItem("AWS (S3, Redshift Basics)", 80, "2 Years"),
                SkillItem("ETL Pipeline Architecture", 90, "3 Years", highlighted = true),
                SkillItem("Data Cleaning & Validation", 94, "3+ Years", highlighted = true)
            )
        ),
        SkillGroup(
            category = "Analytics & Methodologies",
            iconName = "analytics",
            skills = listOf(
                SkillItem("A/B Testing & Hypothesis Testing", 86, "2+ Years", highlighted = true),
                SkillItem("Root-Cause Diagnostic Analysis", 92, "3 Years", highlighted = true),
                SkillItem("Agile / Scrum Methodology", 90, "3+ Years"),
                SkillItem("ServiceNow Platform Tools", 88, "3+ Years")
            )
        ),
        SkillGroup(
            category = "Productivity & Collaboration",
            iconName = "work",
            skills = listOf(
                SkillItem("Microsoft Excel (Advanced, Macros)", 96, "5+ Years", highlighted = true),
                SkillItem("Jira & Confluence", 90, "3+ Years"),
                SkillItem("Git & Version Control", 85, "3 Years"),
                SkillItem("Cross-Functional Stakeholder Communication", 92, "3+ Years", highlighted = true)
            )
        )
    )

    fun getProjectById(id: String): Project? {
        return projects.firstOrNull { it.id == id }
    }
}
