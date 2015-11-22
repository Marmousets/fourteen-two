package org.domuique.fourteentwo

import spock.lang.Specification

class StandingsResourceSpec extends Specification {

    private File getResourceAsFile(String resource) {
        URL url = this.getClass().getResource(resource)
        String filepath = url.getFile().replace('%20', ' ')
        new File(filepath)
    }

    def 'we can divide standings by division'() {
        when:
            def standings = this.getResourceAsFile('/2015/fall/advsundiv.txt').text
            Collection<String> divisions = StandingsResource.divide(standings)
        then:
            divisions.count { true } == 4
    }

    def 'we can extract teams from lines of a schedule file'() {
        when:
            def extractedTeam = StandingsResource.extract(line)
        then:
            extractedTeam == new Team(id: teamId, name: teamName)
        where:
            line                                      | teamId  | teamName
            '72103 14 Balls & a Rack 412 3438 28 123' | '72103' | '14 Balls & a Rack'
            '72105 Hell\'s Face 441 3619 30 121'      | '72105' | 'Hell\'s Face'
            //'72307 Stoopid 2 937 4114 27 152'         | '72307' | 'Stoopid 2' // fails with final #
    }

    def 'extracting teams from non-matching lines returns null'() {
        when:
            Team team = StandingsResource.extract(line)
        then:
            team == null
        where:
            line << [ '',
                      'Division #721',
                      'Standings for Week 10 November 10, 2015' ]
    }

}
