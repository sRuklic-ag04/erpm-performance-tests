# RUN TESTS

```bash
mvn gatling:test -Dgatling.simulationClass=itssc.kontron.erpmperformancetests.simulations.LoadTestSimulation
```

## Idea:
standard flow should be
1. create entities (groups, number directories, profiles, subscribers,...) 
2. collect ids of entities
3. update entity by id
4. delete entity by id

since camunda is in background such flow is not possible

updated flow:
1. create entity (groups, number directories, profiles, subscribers,...) 
2. collect camunda processes ids
3. check how many are completed successfully (one success rate indicator of creation)
4. fetch entity ids
5. update entities (groups, number directories, profiles, subscribers,...) (we expect they are created)
6. collect camunda processes ids
7. check how many are completed successfully (success rate indicator of modification)
8. delete entities (groups, number directories, profiles, subscribers,...)
9. collect camunda processes ids
10. check how many are completed successfully (success rate indicator of deletion)

other possible flow:
1. prepare database with test entities
2. start update/delete camunda processes 
3. collect camunda processes ids
4. check how many are completed successfully (success rate indicator of modification/deletion)

difference from updated flow and the latter is that we create entities for specific test case e.g. 
test_update_group_1, test_delete_group_1 and we are not testing on entities which should have been 
created by first create scenario. Now we can check by findBy query if deletion or creation was successful.

## To think about
Maybe idea of checking provisioning is missing the point of performance testing, maybe not.

## Useful stuff:
- https://github.com/james-willett/gatling-api-crash-course-youtube/
- https://www.youtube.com/watch?v=v-VPV0-u3i4&ab_channel=CodeSlate
