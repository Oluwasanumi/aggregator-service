package com.caspercodes.aggregatorservice.config;

import com.caspercodes.aggregatorservice.model.Resource;
import com.caspercodes.aggregatorservice.model.ResourceType;
import com.caspercodes.aggregatorservice.model.Specialty;
import com.caspercodes.aggregatorservice.model.SubSpecialty;
import com.caspercodes.aggregatorservice.repository.ResourceRepository;
import com.caspercodes.aggregatorservice.repository.SpecialtyRepository;
import com.caspercodes.aggregatorservice.repository.SubSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final SpecialtyRepository specialtyRepository;
    private final SubSpecialtyRepository subSpecialtyRepository;
    private final ResourceRepository resourceRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initializing sample data...");

        if (specialtyRepository.count() > 0) {
            log.info("Database already seeded");
            return;
        }


        Map<String, Specialty> specialties = createSpecialties();


        Map<String, SubSpecialty> subSpecialties = createSubSpecialties(specialties);


        createResources(subSpecialties);

        log.info("Sample data initialization complete");
    }

    private Map<String, Specialty> createSpecialties() {
        log.info("Creating specialties...");

        Map<String, Specialty> specialtyMap = new HashMap<>();

        List<Specialty> specialtyList = Arrays.asList(
                Specialty.builder()
                        .name("Frontend Development")
                        .description("Development of user interfaces and user experience for web applications")
                        .build(),

                Specialty.builder()
                        .name("Backend Development")
                        .description("Server-side logic and database interactions for web applications")
                        .build(),

                Specialty.builder()
                        .name("DevOps")
                        .description("Development operations, automation, and infrastructure management")
                        .build(),

                Specialty.builder()
                        .name("Product Management")
                        .description("Overseeing product development, strategy, and lifecycle")
                        .build(),

                Specialty.builder()
                        .name("Data Science")
                        .description("Analysis and interpretation of complex data using scientific methods")
                        .build(),

                Specialty.builder()
                        .name("Agile Methodologies")
                        .description("Iterative approach to project management and software development")
                        .build()
        );

        specialtyRepository.saveAll(specialtyList);

        for (Specialty specialty : specialtyList) {
            specialtyMap.put(specialty.getName(), specialty);
        }

        return specialtyMap;
    }

    private Map<String, SubSpecialty> createSubSpecialties(Map<String, Specialty> specialties) {
        log.info("Creating subspecialties...");

        Map<String, SubSpecialty> subSpecialtyMap = new HashMap<>();
        List<SubSpecialty> subSpecialtyList = new ArrayList<>();


        Specialty frontend = specialties.get("Frontend Development");
        subSpecialtyList.addAll(Arrays.asList(
                createSubSpecialty("React", "JavaScript library for building user interfaces", frontend),
                createSubSpecialty("Angular", "Platform for building mobile and desktop web applications", frontend),
                createSubSpecialty("Vue.js", "Progressive JavaScript framework for building UIs", frontend),
                createSubSpecialty("HTML/CSS", "Core technologies for building web pages", frontend),
                createSubSpecialty("JavaScript", "Programming language for web development", frontend)
        ));


        Specialty backend = specialties.get("Backend Development");
        subSpecialtyList.addAll(Arrays.asList(
                createSubSpecialty("Java", "Object-oriented programming language", backend),
                createSubSpecialty("Python", "Interpreted high-level programming language", backend),
                createSubSpecialty("Node.js", "JavaScript runtime built on Chrome's V8 engine", backend),
                createSubSpecialty("Spring Boot", "Framework for building Java applications", backend),
                createSubSpecialty("RESTful APIs", "Architectural style for web services", backend),
                createSubSpecialty("SQL", "Standard language for database access", backend)
        ));


        Specialty devops = specialties.get("DevOps");
        subSpecialtyList.addAll(Arrays.asList(
                createSubSpecialty("Docker", "Platform for developing, shipping, and running applications", devops),
                createSubSpecialty("Kubernetes", "Container orchestration system", devops),
                createSubSpecialty("CI/CD", "Continuous integration and continuous delivery", devops),
                createSubSpecialty("AWS", "Amazon Web Services cloud platform", devops),
                createSubSpecialty("Azure", "Microsoft cloud platform", devops)
        ));


        Specialty productMgmt = specialties.get("Product Management");
        subSpecialtyList.addAll(Arrays.asList(
                createSubSpecialty("Product Strategy", "Long-term product planning and vision", productMgmt),
                createSubSpecialty("User Research", "Understanding user needs and behaviors", productMgmt),
                createSubSpecialty("Product Analytics", "Data-driven product decision making", productMgmt),
                createSubSpecialty("Roadmapping", "Planning and prioritizing product development", productMgmt)
        ));


        Specialty dataScience = specialties.get("Data Science");
        subSpecialtyList.addAll(Arrays.asList(
                createSubSpecialty("Machine Learning", "Systems that learn from data", dataScience),
                createSubSpecialty("Data Analysis", "Extracting insights from data", dataScience),
                createSubSpecialty("Big Data", "Processing and analyzing large datasets", dataScience),
                createSubSpecialty("Python for Data Science", "Using Python for data analysis", dataScience),
                createSubSpecialty("Data Visualization", "Visual representation of data", dataScience)
        ));


        Specialty agile = specialties.get("Agile Methodologies");
        subSpecialtyList.addAll(Arrays.asList(
                createSubSpecialty("Scrum", "Framework for product development", agile),
                createSubSpecialty("Kanban", "Visual system for managing work", agile),
                createSubSpecialty("Lean", "Methodology focused on eliminating waste", agile),
                createSubSpecialty("Agile Project Management", "Managing projects using agile principles", agile)
        ));

        subSpecialtyRepository.saveAll(subSpecialtyList);

        for (SubSpecialty subSpecialty : subSpecialtyList) {
            subSpecialtyMap.put(subSpecialty.getName(), subSpecialty);
        }

        return subSpecialtyMap;
    }

    private SubSpecialty createSubSpecialty(String name, String description, Specialty specialty) {
        return SubSpecialty.builder()
                .name(name)
                .description(description)
                .specialty(specialty)
                .build();
    }

    private void createResources(Map<String, SubSpecialty> subSpecialties) {
        log.info("Creating resources...");

        List<Resource> resources = new ArrayList<>();


        SubSpecialty react = subSpecialties.get("React");
        resources.addAll(Arrays.asList(
                createResource(
                        "React - The Complete Guide",
                        "Dive in and learn React.js from scratch! Learn Reactjs, Hooks, Redux, React Routing, Animations, Next.js and more!",
                        "https://www.udemy.com/course/react-the-complete-guide-incl-redux/",
                        ResourceType.UDEMY,
                        "Maximilian Schwarzmüller",
                        "https://img-c.udemycdn.com/course/240x135/1362070_b9a1_2.jpg",
                        4.7,
                        2340,
                        react
                ),
                createResource(
                        "Modern React with Redux",
                        "Master React and Redux with React Router, Webpack, and Create-React-App. Includes Hooks!",
                        "https://www.udemy.com/course/react-redux/",
                        ResourceType.UDEMY,
                        "Stephen Grider",
                        "https://img-c.udemycdn.com/course/240x135/705264_caa9_11.jpg",
                        4.6,
                        3240,
                        react
                ),
                createResource(
                        "React JS Crash Course",
                        "Learn the fundamentals of React including components, state, props, hooks, and more",
                        "https://www.youtube.com/watch?v=w7ejDZ8SWv8",
                        ResourceType.YOUTUBE,
                        "Traversy Media",
                        "https://i.ytimg.com/vi/w7ejDZ8SWv8/hqdefault.jpg",
                        4.8,
                        90,
                        react
                )
        ));


        SubSpecialty java = subSpecialties.get("Java");
        resources.addAll(Arrays.asList(
                createResource(
                        "Java Programming Masterclass",
                        "Learn Java In This Course And Become a Computer Programmer. Obtain valuable Core Java Skills And Java Certification",
                        "https://www.udemy.com/course/java-the-complete-java-developer-course/",
                        ResourceType.UDEMY,
                        "Tim Buchalka",
                        "https://img-c.udemycdn.com/course/240x135/533682_c10c_4.jpg",
                        4.6,
                        5280,
                        java
                ),
                createResource(
                        "Java Programming for Complete Beginners",
                        "Learn Java like a Professional! Start from the basics and go all the way to create your own applications and open source projects!",
                        "https://www.udemy.com/course/java-programming-tutorial-for-beginners/",
                        ResourceType.UDEMY,
                        "in28Minutes Official",
                        "https://img-c.udemycdn.com/course/240x135/842428_3aeb_3.jpg",
                        4.5,
                        1680,
                        java
                ),
                createResource(
                        "Object Oriented Programming in Java Specialization",
                        "Learn object-oriented programming principles and apply them to building software applications",
                        "https://www.coursera.org/specializations/object-oriented-programming",
                        ResourceType.COURSERA,
                        "Duke University",
                        "https://d3njjcbhbojbot.cloudfront.net/api/utilities/v1/imageproxy/https://s3.amazonaws.com/coursera-course-photos/39/918560512211e7b16f79c56e61f489/DUKE-unknown-2.jpg",
                        4.5,
                        2400,
                        java
                )
        ));


        SubSpecialty python = subSpecialties.get("Python");
        resources.addAll(Arrays.asList(
                createResource(
                        "2023 Complete Python Bootcamp From Zero to Hero in Python",
                        "Learn Python like a Professional! Start from the basics and go all the way to creating your own applications and games!",
                        "https://www.udemy.com/course/complete-python-bootcamp/",
                        ResourceType.UDEMY,
                        "Jose Portilla",
                        "https://img-c.udemycdn.com/course/240x135/567828_67d0.jpg",
                        4.6,
                        1440,
                        python
                ),
                createResource(
                        "Python for Everybody Specialization",
                        "Learn to Program and Analyze Data with Python. Develop programs to gather, clean, analyze, and visualize data.",
                        "https://www.coursera.org/specializations/python",
                        ResourceType.COURSERA,
                        "University of Michigan",
                        "https://d3njjcbhbojbot.cloudfront.net/api/utilities/v1/imageproxy/https://s3.amazonaws.com/coursera-course-photos/08/33f720502a11e59e72391aa537f5c9/pythonlearn_thumbnail_1x1.png",
                        4.8,
                        1920,
                        python
                ),
                createResource(
                        "Python Tutorial for Beginners",
                        "Complete Python tutorial for beginners with practical examples.",
                        "https://www.youtube.com/watch?v=_uQrJ0TkZlc",
                        ResourceType.YOUTUBE,
                        "Programming with Mosh",
                        "https://i.ytimg.com/vi/_uQrJ0TkZlc/hqdefault.jpg",
                        4.9,
                        360,
                        python
                )
        ));


        SubSpecialty springBoot = subSpecialties.get("Spring Boot");
        resources.addAll(Arrays.asList(
                createResource(
                        "Spring & Hibernate for Beginners",
                        "Spring 5: Learn Spring 5 Core, AOP, Spring MVC, Spring Security, Spring REST, Spring Boot 2, Thymeleaf, JPA & Hibernate",
                        "https://www.udemy.com/course/spring-hibernate-tutorial/",
                        ResourceType.UDEMY,
                        "Chad Darby",
                        "https://img-c.udemycdn.com/course/240x135/647428_be28_5.jpg",
                        4.6,
                        2520,
                        springBoot
                ),
                createResource(
                        "Building RESTful APIs with Spring Boot",
                        "Learn how to build production-ready RESTful APIs using Spring Boot, Spring Security, and JWT.",
                        "https://www.youtube.com/watch?v=9SGDpanrc8U",
                        ResourceType.YOUTUBE,
                        "Amigoscode",
                        "https://i.ytimg.com/vi/9SGDpanrc8U/hqdefault.jpg",
                        4.7,
                        120,
                        springBoot
                )
        ));


        resourceRepository.saveAll(resources);
    }

    private Resource createResource(
            String title,
            String description,
            String url,
            ResourceType resourceType,
            String author,
            String thumbnailUrl,
            Double rating,
            Integer durationMinutes,
            SubSpecialty subSpecialty
    ) {
        return Resource.builder()
                .title(title)
                .description(description)
                .url(url)
                .resourceType(resourceType)
                .author(author)
                .thumbnailUrl(thumbnailUrl)
                .rating(rating)
                .durationMinutes(durationMinutes)
                .subSpecialty(subSpecialty)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}