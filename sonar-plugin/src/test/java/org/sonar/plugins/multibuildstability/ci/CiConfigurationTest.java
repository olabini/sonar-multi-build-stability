package org.sonar.plugins.multibuildstability.ci;

import org.junit.Before;
import org.junit.Test;

import org.sonar.plugins.multibuildstability.ci.CiConfiguration;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class CiConfigurationTest {
    @Test public void parsingAnEmptyStringGivesAnEmptyList() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("");

        assertThat(result.size(), is(0));
    }

    @Test public void parsingASimpleURLGivesOneEntryWithHudsonDefaultAndNoTitle() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("http://some.ci.com:8080/jobs/foobar");
        assertThat(result.size(), is(1));
        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is(""));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8080/jobs/foobar"));

    }

    @Test public void parsingASimpleURLGivesOneEntryWithADifferentSystem() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("Bamboo:http://some.ci.com:8081/jobs/foobar");
        assertThat(result.size(), is(1));
        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is(""));
        assertThat(cic.getSystem(), is("bamboo"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8081/jobs/foobar"));
    }


    @Test public void parsesASystemOfJenkinsToHudson() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("jenkins:http://some.ci.com:8081/jobs/foobar");
        assertThat(result.size(), is(1));
        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is(""));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8081/jobs/foobar"));

    }

    @Test public void parsingASimpleURLWithOnlyATitleAndAURLGivesHudsonAsDefault() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("[Foobar Jobs]http://some.ci.com:8082/jobs/foobar");
        assertThat(result.size(), is(1));
        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is("Foobar Jobs"));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8082/jobs/foobar"));
    }

    @Test public void parsesAColonInsideTitleCorrectly() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("[Foobar: Jobs]http://some.ci.com:8082/jobs/foobar");
        assertThat(result.size(), is(1));
        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is("Foobar: Jobs"));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8082/jobs/foobar"));
    }

    @Test public void parsesAnEscapedPipeInsideTitleCorrectly() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("[Foobar\\| Jobs]http://some.ci.com:8082/jobs/foobar");
        assertThat(result.size(), is(1));
        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is("Foobar| Jobs"));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8082/jobs/foobar"));
    }

    @Test public void parsesAnOpeningSquareBracketCorrectlyInTitle() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("[Foobar[ Jobs]http://some.ci.com:8082/jobs/foobar");
        assertThat(result.size(), is(1));
        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is("Foobar[ Jobs"));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8082/jobs/foobar"));
    }

    @Test public void parsesAnEscapedOpeningSquareBracketCorrectlyInTitle() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("[Foobar\\[ Jobs]http://some.ci.com:8082/jobs/foobar");
        assertThat(result.size(), is(1));
        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is("Foobar[ Jobs"));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8082/jobs/foobar"));
    }

    @Test public void parsesAnEscapedClosingSquareBracketCorrectlyInTitle() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("[Foobar\\] Jobs]http://some.ci.com:8082/jobs/foobar");
        assertThat(result.size(), is(1));
        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is("Foobar] Jobs"));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8082/jobs/foobar"));
    }

    @Test public void parsingASimpleURLWithATitleAndAURLAndSystemParsesCorrectly() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("[Foobar 2 Jobs]Go:http://some.ci.com:8083/jobs/foobar");
        assertThat(result.size(), is(1));
        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is("Foobar 2 Jobs"));
        assertThat(cic.getSystem(), is("go"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8083/jobs/foobar"));
    }


    @Test public void parsingTwoURLsSeparatedByPipeGivesTwoResults() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("http://some.ci.com:8084/jobs/foobar|http://some.ci.com/jobs/somewhereElse");
        assertThat(result.size(), is(2));

        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is(""));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8084/jobs/foobar"));

        cic = result.get(1);

        assertThat(cic.getTitle(), is(""));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com/jobs/somewhereElse"));
    }

    @Test public void parsingTwoURLsSeparatedByPipeWithWhitespaceGivesTwoResults() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("http://some.ci.com:8084/jobs/foobar |     http://some.ci.com/jobs/somewhereElse");
        assertThat(result.size(), is(2));

        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is(""));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8084/jobs/foobar"));

        cic = result.get(1);

        assertThat(cic.getTitle(), is(""));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com/jobs/somewhereElse"));
    }


    @Test public void parsingTwoURLsSeparatedByNewlineGivesTwoResults() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("http://some.ci.com:8084/jobs/foobar \n  http://some.ci.com/jobs/somewhereElse");
        assertThat(result.size(), is(2));

        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is(""));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8084/jobs/foobar"));

        cic = result.get(1);

        assertThat(cic.getTitle(), is(""));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com/jobs/somewhereElse"));
    }


    @Test public void parsingTwoURLsSeparatedByPipeWithSystemOnOneParsesCorrectly() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("http://some.ci.com:8084/jobs/foobar | cruise:http://some.ci.com/jobs/somewhereElse");
        assertThat(result.size(), is(2));

        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is(""));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8084/jobs/foobar"));

        cic = result.get(1);

        assertThat(cic.getTitle(), is(""));
        assertThat(cic.getSystem(), is("cruise"));
        assertThat(cic.getUrl(), is("http://some.ci.com/jobs/somewhereElse"));
    }


    @Test public void parsingTwoURLsSeparatedByPipeWithSystemOnBothParsesCorrectly() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("bamboo:http://some.ci.com:8084/jobs/foobar | cruise:http://some.ci.com/jobs/somewhereElse");
        assertThat(result.size(), is(2));

        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is(""));
        assertThat(cic.getSystem(), is("bamboo"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8084/jobs/foobar"));

        cic = result.get(1);

        assertThat(cic.getTitle(), is(""));
        assertThat(cic.getSystem(), is("cruise"));
        assertThat(cic.getUrl(), is("http://some.ci.com/jobs/somewhereElse"));
    }

    @Test public void parsingTwoURLsSeparatedByPipeWithOnlyTitleParsesCorrectly() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("[Hello]http://some.ci.com:8084/jobs/foobar | [Goodbye]http://some.ci.com/jobs/somewhereElse");
        assertThat(result.size(), is(2));

        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is("Hello"));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8084/jobs/foobar"));

        cic = result.get(1);

        assertThat(cic.getTitle(), is("Goodbye"));
        assertThat(cic.getSystem(), is("hudson"));
        assertThat(cic.getUrl(), is("http://some.ci.com/jobs/somewhereElse"));
    }

    @Test public void parsingTwoURLsSeparatedByPipeWithBothTitleAndSystemCorrectly() {
        List<CiConfiguration> result = CiConfiguration.parseAllFrom("[Hello]bamboo:http://some.ci.com:8084/jobs/foobar | [Goodbye]go:http://some.ci.com/jobs/somewhereElse");
        assertThat(result.size(), is(2));

        CiConfiguration cic = result.get(0);

        assertThat(cic.getTitle(), is("Hello"));
        assertThat(cic.getSystem(), is("bamboo"));
        assertThat(cic.getUrl(), is("http://some.ci.com:8084/jobs/foobar"));

        cic = result.get(1);

        assertThat(cic.getTitle(), is("Goodbye"));
        assertThat(cic.getSystem(), is("go"));
        assertThat(cic.getUrl(), is("http://some.ci.com/jobs/somewhereElse"));
    }
}
